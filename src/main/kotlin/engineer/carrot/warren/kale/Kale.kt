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

class Kale(private val router: IKaleRouter) : IKale {
    private val LOGGER = loggerFor<Kale>()

    override var parsingStateDelegate: IKaleParsingStateDelegate? = null
        set(value) {
            ModeMessage.Factory.parsingStateDelegate = value
        }

    private val handlers = hashMapOf<Class<*>, IKaleHandler<*>>()

    fun addDefaultParsersAndSerialisers(): Kale {
        router.routeMessageToSerialiser(RawMessage::class, RawMessage.Factory)

        router.routeCommandToParser("PING", PingMessage.Factory)
        router.routeMessageToSerialiser(PingMessage::class, PingMessage.Factory)

        router.routeCommandToParser("PONG", PongMessage.Factory)
        router.routeMessageToSerialiser(PongMessage::class, PongMessage.Factory)

        router.routeCommandToParser("NICK", NickMessage.Factory)
        router.routeMessageToSerialiser(NickMessage::class, NickMessage.Factory)

        router.routeCommandToParser("USER", UserMessage.Factory)
        router.routeMessageToSerialiser(UserMessage::class, UserMessage.Factory)

        router.routeCommandToParser("PASS", PassMessage.Factory)
        router.routeMessageToSerialiser(PassMessage::class, PassMessage.Factory)

        router.routeCommandToParser("QUIT", QuitMessage.Factory)
        router.routeMessageToSerialiser(QuitMessage::class, QuitMessage.Factory)

        router.routeCommandToParser("PART", PartMessage.Factory)
        router.routeMessageToSerialiser(PartMessage::class, PartMessage.Factory)

        router.routeCommandToParser("MODE", ModeMessage.Factory)
        router.routeMessageToSerialiser(ModeMessage::class, ModeMessage.Factory)

        router.routeCommandToParser("PRIVMSG", PrivMsgMessage.Factory)
        router.routeMessageToSerialiser(PrivMsgMessage::class, PrivMsgMessage.Factory)

        router.routeCommandToParser("NOTICE", NoticeMessage.Factory)
        router.routeMessageToSerialiser(NoticeMessage::class, NoticeMessage.Factory)

        router.routeCommandToParser("INVITE", InviteMessage.Factory)
        router.routeMessageToSerialiser(InviteMessage::class, InviteMessage.Factory)

        router.routeCommandToParser("TOPIC", TopicMessage.Factory)
        router.routeMessageToSerialiser(TopicMessage::class, TopicMessage.Factory)

        router.routeCommandToParser("KICK", KickMessage.Factory)
        router.routeMessageToSerialiser(KickMessage::class, KickMessage.Factory)

        router.routeCommandToParserMatcher("JOIN") { (_, _, _, parameters) ->
            when (parameters.size) {
                1,2 -> JoinMessage.Factory
                3 -> ExtendedJoinMessage.Factory
                else -> null
            }
        }
        router.routeMessageToSerialiser(JoinMessage::class, JoinMessage.Factory)
        router.routeMessageToSerialiser(ExtendedJoinMessage::class, ExtendedJoinMessage.Factory)

        router.routeCommandToParserMatcher("CAP") { (_, _, _, parameters) ->
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
        router.routeMessageToSerialiser(CapAckMessage::class, CapAckMessage.Factory)
        router.routeMessageToSerialiser(CapEndMessage::class, CapEndMessage.Factory)
        router.routeMessageToSerialiser(CapLsMessage::class, CapLsMessage.Factory)
        router.routeMessageToSerialiser(CapNakMessage::class, CapNakMessage.Factory)
        router.routeMessageToSerialiser(CapReqMessage::class, CapReqMessage.Factory)
        router.routeMessageToSerialiser(CapNewMessage::class, CapNewMessage.Factory)
        router.routeMessageToSerialiser(CapDelMessage::class, CapDelMessage.Factory)

        router.routeCommandToParserMatcher("BATCH") { (_, _, _, parameters) ->
            val firstCharacterOfFirstParameter = parameters.getOrNull(0)?.getOrNull(0)
            when (firstCharacterOfFirstParameter) {
                CharacterCodes.PLUS -> BatchStartMessage.Factory
                CharacterCodes.MINUS -> BatchEndMessage.Factory
                else -> null
            }
        }
        router.routeMessageToSerialiser(BatchStartMessage::class, BatchStartMessage.Factory)
        router.routeMessageToSerialiser(BatchEndMessage::class, BatchEndMessage.Factory)

        router.routeCommandToParser("AUTHENTICATE", AuthenticateMessage.Factory)
        router.routeMessageToSerialiser(AuthenticateMessage::class, AuthenticateMessage.Factory)

        router.routeCommandToParser("ACCOUNT", AccountMessage.Factory)
        router.routeMessageToSerialiser(AccountMessage::class, AccountMessage.Factory)

        router.routeCommandToParser("AWAY", AwayMessage.Factory)
        router.routeMessageToSerialiser(AwayMessage::class, AwayMessage.Factory)

        router.routeCommandToParser("903", Rpl903Message.Factory)
        router.routeMessageToSerialiser(Rpl903Message::class, Rpl903Message.Factory)

        router.routeCommandToParser("904", QuitMessage.Factory)
        router.routeMessageToSerialiser(QuitMessage::class, QuitMessage.Factory)

        router.routeCommandToParser("905", Rpl905Message.Factory)
        router.routeMessageToSerialiser(Rpl905Message::class, Rpl905Message.Factory)

        router.routeCommandToParser("001", Rpl001Message.Factory)
        router.routeMessageToSerialiser(Rpl001Message::class, Rpl001Message.Factory)

        router.routeCommandToParser("002", Rpl002Message.Factory)
        router.routeMessageToSerialiser(Rpl002Message::class, Rpl002Message.Factory)

        router.routeCommandToParser("003", Rpl003Message.Factory)
        router.routeMessageToSerialiser(Rpl003Message::class, Rpl003Message.Factory)

        router.routeCommandToParser("005", Rpl005Message.Factory)
        router.routeMessageToSerialiser(Rpl005Message::class, Rpl005Message.Factory)

        router.routeCommandToParser("331", Rpl331Message.Factory)
        router.routeMessageToSerialiser(Rpl331Message::class, Rpl331Message.Factory)

        router.routeCommandToParser("332", Rpl332Message.Factory)
        router.routeMessageToSerialiser(Rpl332Message::class, Rpl332Message.Factory)

        router.routeCommandToParser("353", Rpl353Message.Factory)
        router.routeMessageToSerialiser(Rpl353Message::class, Rpl353Message.Factory)

        router.routeCommandToParser("372", Rpl372Message.Factory)
        router.routeMessageToSerialiser(Rpl372Message::class, Rpl372Message.Factory)

        router.routeCommandToParser("375", Rpl375Message.Factory)
        router.routeMessageToSerialiser(Rpl375Message::class, Rpl375Message.Factory)

        router.routeCommandToParser("376", Rpl376Message.Factory)
        router.routeMessageToSerialiser(Rpl376Message::class, Rpl376Message.Factory)

        router.routeCommandToParser("422", Rpl422Message.Factory)
        router.routeMessageToSerialiser(Rpl422Message::class, Rpl422Message.Factory)

        router.routeCommandToParser("471", Rpl471Message.Factory)
        router.routeMessageToSerialiser(Rpl471Message::class, Rpl471Message.Factory)

        router.routeCommandToParser("473", Rpl473Message.Factory)
        router.routeMessageToSerialiser(Rpl473Message::class, Rpl473Message.Factory)

        router.routeCommandToParser("474", Rpl474Message.Factory)
        router.routeMessageToSerialiser(Rpl474Message::class, Rpl474Message.Factory)

        router.routeCommandToParser("475", Rpl475Message.Factory)
        router.routeMessageToSerialiser(Rpl475Message::class, Rpl475Message.Factory)

        return this
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

        handler.handle(message, ircMessage.tags)
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