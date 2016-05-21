package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.message.IrcMessageParser
import engineer.carrot.warren.kale.irc.message.ircv3.*
import engineer.carrot.warren.kale.irc.message.ircv3.sasl.AuthenticateMessage
import engineer.carrot.warren.kale.irc.message.ircv3.sasl.Rpl903Message
import engineer.carrot.warren.kale.irc.message.ircv3.sasl.Rpl904Message
import engineer.carrot.warren.kale.irc.message.ircv3.sasl.Rpl905Message
import engineer.carrot.warren.kale.irc.message.rfc1459.*
import engineer.carrot.warren.kale.irc.message.rpl.*
import engineer.carrot.warren.kale.irc.message.utility.RawMessage

interface IMessageHashingStrategy {

    fun hash(message: IrcMessage): String?

}

interface IKaleParsingStateDelegate {

    fun modeTakesAParameter(isAdding: Boolean, token: Char): Boolean

}

interface IKale {

    fun <T: IMessage> register(handler: IKaleHandler<T>)
    fun process(line: String)
    fun <T: IMessage> serialise(message: T): IrcMessage?

    var parsingStateDelegate: IKaleParsingStateDelegate?

}

class Kale : IKale {
    private val LOGGER = loggerFor<Kale>()

    override var parsingStateDelegate: IKaleParsingStateDelegate? = null
        set(value) {
            ModeMessage.Factory.parsingStateDelegate = value
        }

    private var messageFactories: MutableMap<String, IMessageFactory<*>> = hashMapOf()
    private var messageToFactory: MutableMap<Class<*>, IMessageFactory<*>> = hashMapOf()

    private var messageHashingStrategies: List<IMessageHashingStrategy> = listOf()

    val commandOnlyHashingStrategy = object : IMessageHashingStrategy {
        override fun hash(message: IrcMessage): String? {
            return message.command
        }
    }

    val commandWithSubcommandHashingStrategy = object : IMessageHashingStrategy {
        override fun hash(message: IrcMessage): String? {
            if (message.parameters.size >= 2) {
                return message.command + message.parameters[1]
            }

            return null
        }
    }

    init {
        messageHashingStrategies += commandOnlyHashingStrategy
        messageHashingStrategies += commandWithSubcommandHashingStrategy
    }

    var handlers: MutableMap<String, IKaleHandler<*>> = hashMapOf()

    fun addDefaultMessages(): Kale {
        addMessageFromFactory(RawMessage.Factory)

        addMessageFromFactory(PingMessage.Factory)
        addMessageFromFactory(PongMessage.Factory)
        addMessageFromFactory(NickMessage.Factory)
        addMessageFromFactory(UserMessage.Factory)
        addMessageFromFactory(QuitMessage.Factory)
        addMessageFromFactory(JoinMessage.Factory)
        addMessageFromFactory(PartMessage.Factory)
        addMessageFromFactory(ModeMessage.Factory)
        addMessageFromFactory(PrivMsgMessage.Factory)
        addMessageFromFactory(NoticeMessage.Factory)
        addMessageFromFactory(InviteMessage.Factory)
        addMessageFromFactory(TopicMessage.Factory)
        addMessageFromFactory(KickMessage.Factory)
        addMessageFromFactory(CapAckMessage.Factory)
        addMessageFromFactory(CapEndMessage.Factory)
        addMessageFromFactory(CapLsMessage.Factory)
        addMessageFromFactory(CapNakMessage.Factory)
        addMessageFromFactory(CapReqMessage.Factory)
        addMessageFromFactory(AuthenticateMessage.Factory)
        addMessageFromFactory(Rpl903Message.Factory)
        addMessageFromFactory(Rpl904Message.Factory)
        addMessageFromFactory(Rpl905Message.Factory)

        addMessageFromFactory(Rpl001Message.Factory)
        addMessageFromFactory(Rpl002Message.Factory)
        addMessageFromFactory(Rpl003Message.Factory)
        addMessageFromFactory(Rpl005Message.Factory)
        addMessageFromFactory(Rpl331Message.Factory)
        addMessageFromFactory(Rpl332Message.Factory)
        addMessageFromFactory(Rpl353Message.Factory)
        addMessageFromFactory(Rpl372Message.Factory)
        addMessageFromFactory(Rpl375Message.Factory)
        addMessageFromFactory(Rpl376Message.Factory)
        addMessageFromFactory(Rpl422Message.Factory)
        addMessageFromFactory(Rpl471Message.Factory)
        addMessageFromFactory(Rpl473Message.Factory)
        addMessageFromFactory(Rpl474Message.Factory)
        addMessageFromFactory(Rpl475Message.Factory)

        return this
    }

    fun <T: IMessage> addMessageFromFactory(factory: IMessageFactory<T>) {
        if (messageFactories.containsKey(factory.key)) {
            throw RuntimeException("Tried to add message factory for ${factory.key}, but one already exists!")
        }

        messageFactories.put(factory.key, factory)
        messageToFactory.put(factory.messageType, factory)
    }

    private fun factoryFromMessage(message: Class<*>): IMessageFactory<*>? {
        return messageToFactory[message]
    }


    override fun <T: IMessage> register(handler: IKaleHandler<T>) {
        val command = factoryFromMessage(handler.messageType)?.key ?: throw RuntimeException("couldn't look up factory for handler: $handler")

        if (handlers.containsKey(command)) {
            throw RuntimeException("tried to register a handler when one already exists for $command: $handler")
        }

        handlers.put(command, handler)
    }

    override fun process(line: String) {
        val ircMessage = IrcMessageParser.parse(line)
        if (ircMessage == null) {
            LOGGER.warn("failed to parse line to IrcMessage: $line")
            return
        }

        val factory = findFactoryFor(ircMessage)
        if (factory == null) {
            LOGGER.debug("no factory for: $ircMessage")
            return
        }

        val message = factory.parse(ircMessage)
        if (message == null) {
            LOGGER.warn("factory failed to parse message: $factory $ircMessage")
            return
        }

        val handler = handlers[factory.key]
        if (handler == null) {
            LOGGER.debug("no handler for: $message")
            return
        }

        @Suppress("UNCHECKED_CAST")
        val typedHandler = handler as? IKaleHandler<IMessage> ?: return
        if (!typedHandler.messageType.isInstance(message)) {
            LOGGER.warn("tried to pass wrong type to handler: ${message.javaClass} to ${handler.messageType}")
            return
        }

        typedHandler.handle(message)
    }

    private fun findFactoryFor(ircMessage: IrcMessage): IMessageFactory<*>? {
        for (strategy in messageHashingStrategies) {
            val hash = strategy.hash(ircMessage)
            if (hash != null) {
                val factory = messageFactories[hash]
                if (factory != null) {
                    return factory
                }
            }
        }

        return null
    }

    override fun <T: IMessage> serialise(message: T): IrcMessage? {
        @Suppress("UNCHECKED_CAST")
        val factory = factoryFromMessage(message.javaClass) as? IMessageFactory<IMessage>
        if (factory == null) {
            LOGGER.warn("failed to find factory for message serialisation: $message")
            return null
        }

        return factory.serialise(message)
    }

}