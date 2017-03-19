package chat.willow.kale

import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.tag.ITagStore

typealias IMetadataStore = ITagStore

interface IKaleIrcMessageHandler {

    fun handle(message: IrcMessage, metadata: IMetadataStore)

}

interface IKaleMessageHandler<in T> {

    fun handle(message: T, metadata: IMetadataStore)

}


interface ICommand {

    val command: String

}

interface ISubcommand {

    val subcommand: String
}

data class IrcMessageComponents(val parameters: List<String> = listOf(), val tags: Map<String, String?> = mapOf(), val prefix: String? = null)

class KaleParseOnlyHandler<in T>(private val parser: IMessageParser<T>) : IKaleIrcMessageHandler, IKaleMessageHandler<T> {

    private val LOGGER = loggerFor<KaleParseOnlyHandler<T>>()

    override fun handle(message: IrcMessage, metadata: IMetadataStore) {
        val parsedMessage = parser.parse(message) ?: return

        handle(parsedMessage, metadata)
    }

    override fun handle(message: T, metadata: IMetadataStore) {
        LOGGER.info("no handler for: $message")
    }

}

abstract class KaleHandler<in T>(private val parser: IMessageParser<T>) : IKaleIrcMessageHandler, IKaleMessageHandler<T> {

    override fun handle(message: IrcMessage, metadata: IMetadataStore) {
        val parsedMessage = parser.parse(message) ?: return

        handle(parsedMessage, metadata)
    }

}

class KaleSubcommandHandler(private val handlers: Map<String, IKaleIrcMessageHandler>, val subcommandPosition: Int = 0) : IKaleIrcMessageHandler {

    private val LOGGER = loggerFor<KaleSubcommandHandler>()

    override fun handle(message: IrcMessage, metadata: IMetadataStore) {
        if (message.parameters.isEmpty()) {
            return
        }

        val subcommand = message.parameters[0]

        val handler = handlers[subcommand]
        if (handler == null) {
            LOGGER.warn("no handler for subcommand $subcommand")
            return
        }

        handler.handle(message, metadata)
    }

}