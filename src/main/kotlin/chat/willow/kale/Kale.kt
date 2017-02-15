package chat.willow.kale

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.message.IrcMessageParser
import chat.willow.kale.irc.message.rfc1459.ModeMessage
import chat.willow.kale.irc.metadata.MetadataStore
import chat.willow.kale.irc.metadata.RawTagsMetadata

interface IKale {

    fun <T: IMessage> register(handler: IKaleHandler<T>)
    fun <T: IMessage> unregister(handler: IKaleHandler<T>)
    fun <M: IMessage> handlerFor(messageClass: Class<M>): IKaleHandler<M>?
    fun process(line: String)
    fun serialise(message: Any): IrcMessage?

    var parsingStateDelegate: IKaleParsingStateDelegate?

}

interface IKaleParsingStateDelegate {

    fun modeTakesAParameter(isAdding: Boolean, token: Char): Boolean

}

class Kale(private val router: IKaleRouter) : IKale {
    private val LOGGER = loggerFor<Kale>()

    override var parsingStateDelegate: IKaleParsingStateDelegate? = null
        set(value) {
            ModeMessage.parsingStateDelegate = value
        }

    private val handlers = hashMapOf<Class<*>, IKaleHandler<*>>()

    override fun <M: IMessage> register(handler: IKaleHandler<M>) {
        handlers.put(handler.messageType, handler)
    }

    override fun <M: IMessage> unregister(handler: IKaleHandler<M>) {
        handlers.remove(handler.messageType)
    }

    override fun process(line: String) {
        val ircMessage = IrcMessageParser.parse(line)
        if (ircMessage == null) {
            LOGGER.warn("failed to parse line to IrcMessage: $line")
            return
        }

        val factory = router.parserFor(ircMessage)
        if (factory == null) {
            LOGGER.debug("no factory for: $ircMessage")
            return
        }

        val message = factory.parse(ircMessage)
        if (message == null) {
            LOGGER.warn("factory failed to parse message: $factory $ircMessage")
            return
        }

        val handler = findHandlerFor(message)
        if (handler == null) {
            LOGGER.debug("no handler for: $message")
            return
        }

        val metadata = constructMetadataStore(ircMessage)

        handler.handle(message, metadata)
    }

    private fun constructMetadataStore(message: IrcMessage): MetadataStore {
        val metadata = MetadataStore()

        metadata.store(RawTagsMetadata(tags = message.tags))

        return metadata
    }

    private fun <M: IMessage> findHandlerFor(message: M): IKaleHandler<M>? {
        @Suppress("UNCHECKED_CAST")
        return handlers[message.javaClass] as? IKaleHandler<M>
    }

    override fun <M: IMessage> handlerFor(messageClass: Class<M>): IKaleHandler<M>? {
        @Suppress("UNCHECKED_CAST")
        return handlers[messageClass] as? IKaleHandler<M>
    }

    override fun serialise(message: Any): IrcMessage? {
        val factory = router.serialiserFor(message.javaClass)
        if (factory == null) {
            LOGGER.warn("failed to find factory for message serialisation: $message")
            return null
        }

        return factory.serialise(message)
    }

}