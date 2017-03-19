package chat.willow.kale

import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.message.IrcMessageParser
import chat.willow.kale.irc.message.rfc1459.ModeMessage
import chat.willow.kale.irc.tag.IKaleTagRouter
import chat.willow.kale.irc.tag.Tag
import chat.willow.kale.irc.tag.TagStore

interface IKale {

    fun process(line: String)

    fun <M: Any> serialise(message: M): IrcMessage?

    var parsingStateDelegate: IKaleParsingStateDelegate?

}

interface IKaleParsingStateDelegate {

    fun modeTakesAParameter(isAdding: Boolean, token: Char): Boolean

}

class Kale(val router: IKaleRouter, private val tagRouter: IKaleTagRouter) : IKale {
    private val LOGGER = loggerFor<Kale>()

    override var parsingStateDelegate: IKaleParsingStateDelegate? = null
        set(value) {
            ModeMessage.parsingStateDelegate = value
        }

    override fun process(line: String) {
        val ircMessage = IrcMessageParser.parse(line)
        if (ircMessage == null) {
            LOGGER.warn("failed to parse line to IrcMessage: $line")
            return
        }

        val handler = router.handlerFor(ircMessage.command)
        if (handler == null) {
            LOGGER.debug("no handler for: ${ircMessage.command}")
            return
        }

        val metadata = constructMetadataStore(ircMessage)

        handler.handle(ircMessage, metadata)
    }

    private fun constructMetadataStore(message: IrcMessage): TagStore {
        val metadata = TagStore()

        for ((key, value) in message.tags) {
            val tag = Tag(key, value)

            val factory = tagRouter.parserFor(tag.name)
            if (factory == null) {
                LOGGER.debug("no parser for tag $tag")
                continue
            }

            val parsedTag = factory.parse(tag)
            if (parsedTag == null) {
                LOGGER.warn("factory failed to parse tag: $factory $tag")
                continue
            }

            metadata.store(parsedTag)
        }

        return metadata
    }

    override fun <M: Any> serialise(message: M): IrcMessage? {
        @Suppress("UNCHECKED_CAST")
        val factory = router.serialiserFor(message::class.java) as? IMessageSerialiser<M>
        if (factory == null) {
            LOGGER.warn("failed to find factory for message serialisation: $message")
            return null
        }

        return factory.serialise(message)
    }

}