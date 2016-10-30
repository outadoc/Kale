package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.*
import engineer.carrot.warren.kale.irc.message.extension.account_notify.AccountMessage
import engineer.carrot.warren.kale.irc.message.extension.away_notify.AwayMessage
import engineer.carrot.warren.kale.irc.message.extension.batch.BatchEndMessage
import engineer.carrot.warren.kale.irc.message.extension.batch.BatchStartMessage
import engineer.carrot.warren.kale.irc.message.extension.cap.*
import engineer.carrot.warren.kale.irc.message.extension.extended_join.ExtendedJoinMessage
import engineer.carrot.warren.kale.irc.message.extension.sasl.AuthenticateMessage
import engineer.carrot.warren.kale.irc.message.extension.sasl.Rpl903Message
import engineer.carrot.warren.kale.irc.message.extension.sasl.Rpl904Message
import engineer.carrot.warren.kale.irc.message.extension.sasl.Rpl905Message
import engineer.carrot.warren.kale.irc.message.rfc1459.*
import engineer.carrot.warren.kale.irc.message.rfc1459.rpl.*
import engineer.carrot.warren.kale.irc.message.utility.RawMessage

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


interface IParserRouter {

    fun parserFor(message: IrcMessage): IMessageParser<*>?

}


class Kale : IKale {
    private val LOGGER = loggerFor<Kale>()

    override var parsingStateDelegate: IKaleParsingStateDelegate? = null
        set(value) {
            ModeMessage.Factory.parsingStateDelegate = value
        }

    private var commandRouters = hashMapOf<String, IParserRouter>()
    private var messageSerialisers = hashMapOf<Class<*>, IMessageSerialiser<*>>()

    var handlers: MutableMap<Class<*>, IKaleHandler<*>> = hashMapOf()

    fun addDefaultMessages(): Kale {
        routeMessageToSerialiser(RawMessage::class.java, RawMessage.Factory)

        routeCommandAndMessageToFactory("PING", PingMessage::class.java, PingMessage.Factory, PingMessage.Factory)
        routeCommandAndMessageToFactory("PONG", PongMessage::class.java, PongMessage.Factory, PongMessage.Factory)
        routeCommandAndMessageToFactory("NICK", NickMessage::class.java, NickMessage.Factory, NickMessage.Factory)
        routeCommandAndMessageToFactory("USER", UserMessage::class.java, UserMessage.Factory, UserMessage.Factory)
        routeCommandAndMessageToFactory("PASS", PassMessage::class.java, PassMessage.Factory, PassMessage.Factory)
        routeCommandAndMessageToFactory("QUIT", QuitMessage::class.java, QuitMessage.Factory, QuitMessage.Factory)
        routeCommandAndMessageToFactory("PART", PartMessage::class.java, PartMessage.Factory, PartMessage.Factory)
        routeCommandAndMessageToFactory("MODE", ModeMessage::class.java, ModeMessage.Factory, ModeMessage.Factory)
        routeCommandAndMessageToFactory("PRIVMSG", PrivMsgMessage::class.java, PrivMsgMessage.Factory, PrivMsgMessage.Factory)
        routeCommandAndMessageToFactory("NOTICE", NoticeMessage::class.java, NoticeMessage.Factory, NoticeMessage.Factory)
        routeCommandAndMessageToFactory("INVITE", InviteMessage::class.java, InviteMessage.Factory, InviteMessage.Factory)
        routeCommandAndMessageToFactory("TOPIC", TopicMessage::class.java, TopicMessage.Factory, TopicMessage.Factory)
        routeCommandAndMessageToFactory("KICK", KickMessage::class.java, KickMessage.Factory, KickMessage.Factory)

        routeCommandToParsers("JOIN") { (tags, prefix, command, parameters) ->
            when (parameters.size) {
                1,2 -> JoinMessage.Factory
                3 -> ExtendedJoinMessage.Factory
                else -> null
            }
        }
        routeMessageToSerialiser(JoinMessage::class.java, JoinMessage.Factory)
        routeMessageToSerialiser(ExtendedJoinMessage::class.java, ExtendedJoinMessage.Factory)

        routeCommandToParsers("CAP") { (tags, prefix, command, parameters) ->
            val subcommand = parameters.getOrNull(1)
            when (subcommand) {
                "ACK" -> CapAckMessage.Factory
                "END" -> CapEndMessage.Factory
                "LS" -> CapLsMessage.Factory
                "NAK" -> CapNakMessage.Factory
                "REQ" -> CapReqMessage.Factory
                "NEW" -> CapNewMessage.Factory
                "DEL" -> CapDelMessage.Factory
                else -> null
            }
        }
        routeMessageToSerialiser(CapAckMessage::class.java, CapAckMessage.Factory)
        routeMessageToSerialiser(CapEndMessage::class.java, CapEndMessage.Factory)
        routeMessageToSerialiser(CapLsMessage::class.java, CapLsMessage.Factory)
        routeMessageToSerialiser(CapNakMessage::class.java, CapNakMessage.Factory)
        routeMessageToSerialiser(CapReqMessage::class.java, CapReqMessage.Factory)
        routeMessageToSerialiser(CapNewMessage::class.java, CapNewMessage.Factory)
        routeMessageToSerialiser(CapDelMessage::class.java, CapDelMessage.Factory)

        routeCommandToParsers("BATCH") { (tags, prefix, command, parameters) ->
            val firstCharacterOfFirstParameter = parameters.getOrNull(0)?.getOrNull(0)
            when (firstCharacterOfFirstParameter) {
                CharacterCodes.PLUS -> BatchStartMessage.Factory
                CharacterCodes.MINUS -> BatchEndMessage.Factory
                else -> null
            }
        }
        routeMessageToSerialiser(BatchStartMessage::class.java, BatchStartMessage.Factory)
        routeMessageToSerialiser(BatchEndMessage::class.java, BatchEndMessage.Factory)

        routeCommandAndMessageToFactory("AUTHENTICATE", AuthenticateMessage::class.java, AuthenticateMessage.Factory, AuthenticateMessage.Factory)
        routeCommandAndMessageToFactory("ACCOUNT", AccountMessage::class.java, AccountMessage.Factory, AccountMessage.Factory)
        routeCommandAndMessageToFactory("AWAY", AwayMessage::class.java, AwayMessage.Factory, AwayMessage.Factory)

        routeCommandAndMessageToFactory("903", Rpl903Message::class.java, Rpl903Message.Factory, Rpl903Message.Factory)
        routeCommandAndMessageToFactory("904", Rpl904Message::class.java, Rpl904Message.Factory, Rpl904Message.Factory)
        routeCommandAndMessageToFactory("905", Rpl905Message::class.java, Rpl905Message.Factory, Rpl905Message.Factory)
        routeCommandAndMessageToFactory("001", Rpl001Message::class.java, Rpl001Message.Factory, Rpl001Message.Factory)
        routeCommandAndMessageToFactory("002", Rpl002Message::class.java, Rpl002Message.Factory, Rpl002Message.Factory)
        routeCommandAndMessageToFactory("003", Rpl003Message::class.java, Rpl003Message.Factory, Rpl003Message.Factory)
        routeCommandAndMessageToFactory("005", Rpl005Message::class.java, Rpl005Message.Factory, Rpl005Message.Factory)
        routeCommandAndMessageToFactory("331", Rpl331Message::class.java, Rpl331Message.Factory, Rpl331Message.Factory)
        routeCommandAndMessageToFactory("332", Rpl332Message::class.java, Rpl332Message.Factory, Rpl332Message.Factory)
        routeCommandAndMessageToFactory("353", Rpl353Message::class.java, Rpl353Message.Factory, Rpl353Message.Factory)
        routeCommandAndMessageToFactory("372", Rpl372Message::class.java, Rpl372Message.Factory, Rpl372Message.Factory)
        routeCommandAndMessageToFactory("375", Rpl375Message::class.java, Rpl375Message.Factory, Rpl375Message.Factory)
        routeCommandAndMessageToFactory("376", Rpl376Message::class.java, Rpl376Message.Factory, Rpl376Message.Factory)
        routeCommandAndMessageToFactory("422", Rpl422Message::class.java, Rpl422Message.Factory, Rpl422Message.Factory)
        routeCommandAndMessageToFactory("471", Rpl471Message::class.java, Rpl471Message.Factory, Rpl471Message.Factory)
        routeCommandAndMessageToFactory("473", Rpl473Message::class.java, Rpl473Message.Factory, Rpl473Message.Factory)
        routeCommandAndMessageToFactory("474", Rpl474Message::class.java, Rpl474Message.Factory, Rpl474Message.Factory)
        routeCommandAndMessageToFactory("475", Rpl475Message::class.java, Rpl475Message.Factory, Rpl475Message.Factory)

        return this
    }

    fun <T: IMessage> routeCommandToParser(command: String, parser: IMessageParser<T>) {
        val parserRouter = object : IParserRouter {
            override fun parserFor(message: IrcMessage): IMessageParser<*>? {
                return parser
            }
        }

        commandRouters[command] = parserRouter
    }

    fun routeCommandToParsers(command: String, matcher: (IrcMessage) -> (IMessageParser<*>?)) {
        commandRouters[command] = object : IParserRouter {
            override fun parserFor(message: IrcMessage): IMessageParser<*>? {
                return matcher(message)
            }
        }
    }

    fun <M> routeMessageToSerialiser(messageClass: Class<M>, serialiser: IMessageSerialiser<M>) {
        messageSerialisers[messageClass] = serialiser
    }

    fun <M: IMessage> routeCommandAndMessageToFactory(command: String, messageClass: Class<M>, parser: IMessageParser<M>, serialiser: IMessageSerialiser<M>) {
        routeCommandToParser(command, parser)
        routeMessageToSerialiser(messageClass, serialiser)
    }

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

        val factory = findParserFor(ircMessage)
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

        handler.handle(message, ircMessage.tags)
    }

    private fun findParserFor(ircMessage: IrcMessage): IMessageParser<*>? {
        return commandRouters[ircMessage.command]?.parserFor(ircMessage)
    }

    private fun <M> findSerialiserFor(messageClass: Class<M>): IMessageSerialiser<M>? {
        @Suppress("UNCHECKED_CAST")
        return messageSerialisers[messageClass] as? IMessageSerialiser<M>
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
        val factory = findSerialiserFor(message.javaClass)
        if (factory == null) {
            LOGGER.warn("failed to find factory for message serialisation: $message")
            return null
        }

        return factory.serialise(message)
    }

}