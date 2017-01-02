package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.message.extension.account_notify.AccountMessage
import engineer.carrot.warren.kale.irc.message.extension.away_notify.AwayMessage
import engineer.carrot.warren.kale.irc.message.extension.batch.BatchEndMessage
import engineer.carrot.warren.kale.irc.message.extension.batch.BatchStartMessage
import engineer.carrot.warren.kale.irc.message.extension.cap.*
import engineer.carrot.warren.kale.irc.message.extension.extended_join.ExtendedJoinMessage
import engineer.carrot.warren.kale.irc.message.extension.sasl.AuthenticateMessage
import engineer.carrot.warren.kale.irc.message.extension.sasl.Rpl903Message
import engineer.carrot.warren.kale.irc.message.extension.sasl.Rpl905Message
import engineer.carrot.warren.kale.irc.message.rfc1459.*
import engineer.carrot.warren.kale.irc.message.rfc1459.rpl.*
import engineer.carrot.warren.kale.irc.message.utility.RawMessage
import kotlin.reflect.KClass

typealias ParserMatcher = (IrcMessage) -> (IMessageParser<*>?)

interface IParserRouter {

    fun parserFor(message: IrcMessage): IMessageParser<*>?

}

interface IKaleRouter {

    fun <M: IMessage> routeCommandToParser(command: String, parser: IMessageParser<M>)
    fun routeCommandToParserMatcher(command: String, matcher: ParserMatcher)
    fun parserFor(ircMessage: IrcMessage): IMessageParser<*>?

    fun <M: Any> routeMessageToSerialiser(messageClass: KClass<M>, serialiser: IMessageSerialiser<M>)
    fun <M: Any> serialiserFor(messageClass: Class<M>): IMessageSerialiser<M>?

}

class KaleRouter : IKaleRouter {

    private val commandsToParsers = hashMapOf<String, IParserRouter>()
    private val messagesToSerialisers = hashMapOf<Class<*>, IMessageSerialiser<*>>()

    override fun <T: IMessage> routeCommandToParser(command: String, parser: IMessageParser<T>) {
        val parserRouter = object : IParserRouter {
            override fun parserFor(message: IrcMessage): IMessageParser<*>? {
                return parser
            }
        }

        commandsToParsers[command] = parserRouter
    }

    override fun routeCommandToParserMatcher(command: String, matcher: ParserMatcher) {
        commandsToParsers[command] = object : IParserRouter {
            override fun parserFor(message: IrcMessage): IMessageParser<*>? {
                return matcher(message)
            }
        }
    }

    override fun parserFor(ircMessage: IrcMessage): IMessageParser<*>? {
        return commandsToParsers[ircMessage.command]?.parserFor(ircMessage)
    }

    override fun <M : Any> routeMessageToSerialiser(messageClass: KClass<M>, serialiser: IMessageSerialiser<M>) {
        messagesToSerialisers[messageClass.java] = serialiser
    }

    override fun <M: Any> serialiserFor(messageClass: Class<M>): IMessageSerialiser<M>? {
        @Suppress("UNCHECKED_CAST")
        return messagesToSerialisers[messageClass] as? IMessageSerialiser<M>
    }

    fun useDefaults(): KaleRouter {
        routeMessageToSerialiser(RawMessage::class, RawMessage.Factory)

        routeCommandToParser("PING", PingMessage.Factory)
        routeMessageToSerialiser(PingMessage::class, PingMessage.Factory)

        routeCommandToParser("PONG", PongMessage.Factory)
        routeMessageToSerialiser(PongMessage::class, PongMessage.Factory)

        routeCommandToParser("NICK", NickMessage.Factory)
        routeMessageToSerialiser(NickMessage::class, NickMessage.Factory)

        routeCommandToParser("USER", UserMessage.Factory)
        routeMessageToSerialiser(UserMessage::class, UserMessage.Factory)

        routeCommandToParser("PASS", PassMessage.Factory)
        routeMessageToSerialiser(PassMessage::class, PassMessage.Factory)

        routeCommandToParser("QUIT", QuitMessage.Factory)
        routeMessageToSerialiser(QuitMessage::class, QuitMessage.Factory)

        routeCommandToParser("PART", PartMessage.Factory)
        routeMessageToSerialiser(PartMessage::class, PartMessage.Factory)

        routeCommandToParser("MODE", ModeMessage.Factory)
        routeMessageToSerialiser(ModeMessage::class, ModeMessage.Factory)

        routeCommandToParser("PRIVMSG", PrivMsgMessage.Factory)
        routeMessageToSerialiser(PrivMsgMessage::class, PrivMsgMessage.Factory)

        routeCommandToParser("NOTICE", NoticeMessage.Factory)
        routeMessageToSerialiser(NoticeMessage::class, NoticeMessage.Factory)

        routeCommandToParser("INVITE", InviteMessage.Factory)
        routeMessageToSerialiser(InviteMessage::class, InviteMessage.Factory)

        routeCommandToParser("TOPIC", TopicMessage.Factory)
        routeMessageToSerialiser(TopicMessage::class, TopicMessage.Factory)

        routeCommandToParser("KICK", KickMessage.Factory)
        routeMessageToSerialiser(KickMessage::class, KickMessage.Factory)

        routeCommandToParserMatcher("JOIN") { (_, _, _, parameters) ->
            when (parameters.size) {
                1,2 -> JoinMessage.Factory
                3 -> ExtendedJoinMessage.Factory
                else -> null
            }
        }
        routeMessageToSerialiser(JoinMessage::class, JoinMessage.Factory)
        routeMessageToSerialiser(ExtendedJoinMessage::class, ExtendedJoinMessage.Factory)

        routeCommandToParserMatcher("CAP") { (_, _, _, parameters) ->
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
        routeMessageToSerialiser(CapAckMessage::class, CapAckMessage.Factory)
        routeMessageToSerialiser(CapEndMessage::class, CapEndMessage.Factory)
        routeMessageToSerialiser(CapLsMessage::class, CapLsMessage.Factory)
        routeMessageToSerialiser(CapNakMessage::class, CapNakMessage.Factory)
        routeMessageToSerialiser(CapReqMessage::class, CapReqMessage.Factory)
        routeMessageToSerialiser(CapNewMessage::class, CapNewMessage.Factory)
        routeMessageToSerialiser(CapDelMessage::class, CapDelMessage.Factory)

        routeCommandToParserMatcher("BATCH") { (_, _, _, parameters) ->
            val firstCharacterOfFirstParameter = parameters.getOrNull(0)?.getOrNull(0)
            when (firstCharacterOfFirstParameter) {
                CharacterCodes.PLUS -> BatchStartMessage.Factory
                CharacterCodes.MINUS -> BatchEndMessage.Factory
                else -> null
            }
        }
        routeMessageToSerialiser(BatchStartMessage::class, BatchStartMessage.Factory)
        routeMessageToSerialiser(BatchEndMessage::class, BatchEndMessage.Factory)

        routeCommandToParser("AUTHENTICATE", AuthenticateMessage.Factory)
        routeMessageToSerialiser(AuthenticateMessage::class, AuthenticateMessage.Factory)

        routeCommandToParser("ACCOUNT", AccountMessage.Factory)
        routeMessageToSerialiser(AccountMessage::class, AccountMessage.Factory)

        routeCommandToParser("AWAY", AwayMessage.Factory)
        routeMessageToSerialiser(AwayMessage::class, AwayMessage.Factory)

        routeCommandToParser("903", Rpl903Message.Factory)
        routeMessageToSerialiser(Rpl903Message::class, Rpl903Message.Factory)

        routeCommandToParser("904", QuitMessage.Factory)
        routeMessageToSerialiser(QuitMessage::class, QuitMessage.Factory)

        routeCommandToParser("905", Rpl905Message.Factory)
        routeMessageToSerialiser(Rpl905Message::class, Rpl905Message.Factory)

        routeCommandToParser("001", Rpl001Message.Factory)
        routeMessageToSerialiser(Rpl001Message::class, Rpl001Message.Factory)

        routeCommandToParser("002", Rpl002Message.Factory)
        routeMessageToSerialiser(Rpl002Message::class, Rpl002Message.Factory)

        routeCommandToParser("003", Rpl003Message.Factory)
        routeMessageToSerialiser(Rpl003Message::class, Rpl003Message.Factory)

        routeCommandToParser("005", Rpl005Message.Factory)
        routeMessageToSerialiser(Rpl005Message::class, Rpl005Message.Factory)

        routeCommandToParser("331", Rpl331Message.Factory)
        routeMessageToSerialiser(Rpl331Message::class, Rpl331Message.Factory)

        routeCommandToParser("332", Rpl332Message.Factory)
        routeMessageToSerialiser(Rpl332Message::class, Rpl332Message.Factory)

        routeCommandToParser("353", Rpl353Message.Factory)
        routeMessageToSerialiser(Rpl353Message::class, Rpl353Message.Factory)

        routeCommandToParser("372", Rpl372Message.Factory)
        routeMessageToSerialiser(Rpl372Message::class, Rpl372Message.Factory)

        routeCommandToParser("375", Rpl375Message.Factory)
        routeMessageToSerialiser(Rpl375Message::class, Rpl375Message.Factory)

        routeCommandToParser("376", Rpl376Message.Factory)
        routeMessageToSerialiser(Rpl376Message::class, Rpl376Message.Factory)

        routeCommandToParser("422", Rpl422Message.Factory)
        routeMessageToSerialiser(Rpl422Message::class, Rpl422Message.Factory)

        routeCommandToParser("471", Rpl471Message.Factory)
        routeMessageToSerialiser(Rpl471Message::class, Rpl471Message.Factory)

        routeCommandToParser("473", Rpl473Message.Factory)
        routeMessageToSerialiser(Rpl473Message::class, Rpl473Message.Factory)

        routeCommandToParser("474", Rpl474Message.Factory)
        routeMessageToSerialiser(Rpl474Message::class, Rpl474Message.Factory)

        routeCommandToParser("475", Rpl475Message.Factory)
        routeMessageToSerialiser(Rpl475Message::class, Rpl475Message.Factory)

        return this
    }

}