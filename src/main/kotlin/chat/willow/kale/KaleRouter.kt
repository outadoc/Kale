package chat.willow.kale

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.message.extension.account_notify.AccountMessage
import chat.willow.kale.irc.message.extension.away_notify.AwayMessage
import chat.willow.kale.irc.message.extension.batch.BatchEndMessage
import chat.willow.kale.irc.message.extension.batch.BatchStartMessage
import chat.willow.kale.irc.message.extension.cap.*
import chat.willow.kale.irc.message.extension.extended_join.ExtendedJoinMessage
import chat.willow.kale.irc.message.extension.monitor.*
import chat.willow.kale.irc.message.extension.monitor.rpl.*
import chat.willow.kale.irc.message.extension.sasl.AuthenticateMessage
import chat.willow.kale.irc.message.extension.sasl.Rpl903Message
import chat.willow.kale.irc.message.extension.sasl.Rpl904Message
import chat.willow.kale.irc.message.extension.sasl.Rpl905Message
import chat.willow.kale.irc.message.rfc1459.*
import chat.willow.kale.irc.message.rfc1459.rpl.*
import chat.willow.kale.irc.message.utility.RawMessage
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
        routeMessageToSerialiser(RawMessage::class, RawMessage)

        routeCommandToParser("PING", PingMessage)
        routeMessageToSerialiser(PingMessage::class, PingMessage)

        routeCommandToParser("PONG", PongMessage)
        routeMessageToSerialiser(PongMessage::class, PongMessage)

        routeCommandToParser("NICK", NickMessage)
        routeMessageToSerialiser(NickMessage::class, NickMessage)

        routeCommandToParser("USER", UserMessage)
        routeMessageToSerialiser(UserMessage::class, UserMessage)

        routeCommandToParser("PASS", PassMessage)
        routeMessageToSerialiser(PassMessage::class, PassMessage)

        routeCommandToParser("QUIT", QuitMessage)
        routeMessageToSerialiser(QuitMessage::class, QuitMessage)

        routeCommandToParser("PART", PartMessage)
        routeMessageToSerialiser(PartMessage::class, PartMessage)

        routeCommandToParser("MODE", ModeMessage)
        routeMessageToSerialiser(ModeMessage::class, ModeMessage)

        routeCommandToParser("PRIVMSG", PrivMsgMessage)
        routeMessageToSerialiser(PrivMsgMessage::class, PrivMsgMessage)

        routeCommandToParser("NOTICE", NoticeMessage)
        routeMessageToSerialiser(NoticeMessage::class, NoticeMessage)

        routeCommandToParser("INVITE", InviteMessage)
        routeMessageToSerialiser(InviteMessage::class, InviteMessage)

        routeCommandToParser("TOPIC", TopicMessage)
        routeMessageToSerialiser(TopicMessage::class, TopicMessage)

        routeCommandToParser("KICK", KickMessage)
        routeMessageToSerialiser(KickMessage::class, KickMessage)

        routeCommandToParserMatcher("JOIN") { (_, _, _, parameters) ->
            when (parameters.size) {
                1,2 -> JoinMessage
                3 -> ExtendedJoinMessage
                else -> null
            }
        }
        routeMessageToSerialiser(JoinMessage::class, JoinMessage)
        routeMessageToSerialiser(ExtendedJoinMessage::class, ExtendedJoinMessage)

        routeCommandToParserMatcher("CAP") { (_, _, _, parameters) ->
            val subcommand = parameters.getOrNull(1)
            when (subcommand) {
                "ACK" -> CapAckMessage
                "END" -> CapEndMessage
                "LS" -> CapLsMessage
                "NAK" -> CapNakMessage
                "REQ" -> CapReqMessage
                "NEW" -> CapNewMessage
                "DEL" -> CapDelMessage
                else -> null
            }
        }
        routeMessageToSerialiser(CapAckMessage::class, CapAckMessage)
        routeMessageToSerialiser(CapEndMessage::class, CapEndMessage)
        routeMessageToSerialiser(CapLsMessage::class, CapLsMessage)
        routeMessageToSerialiser(CapNakMessage::class, CapNakMessage)
        routeMessageToSerialiser(CapReqMessage::class, CapReqMessage)
        routeMessageToSerialiser(CapNewMessage::class, CapNewMessage)
        routeMessageToSerialiser(CapDelMessage::class, CapDelMessage)

        routeCommandToParserMatcher("BATCH") { (_, _, _, parameters) ->
            val firstCharacterOfFirstParameter = parameters.getOrNull(0)?.getOrNull(0)
            when (firstCharacterOfFirstParameter) {
                CharacterCodes.PLUS -> BatchStartMessage
                CharacterCodes.MINUS -> BatchEndMessage
                else -> null
            }
        }
        routeMessageToSerialiser(BatchStartMessage::class, BatchStartMessage)
        routeMessageToSerialiser(BatchEndMessage::class, BatchEndMessage)

        routeCommandToParserMatcher("MONITOR") { (_, _, _, parameters) ->
            val subCommand = parameters.getOrNull(0)?.getOrNull(0)
            when (subCommand) {
                CharacterCodes.PLUS -> MonitorAddMessage
                CharacterCodes.MINUS -> MonitorRemoveMessage
                'C' -> MonitorClearMessage
                'L' -> MonitorListMessage
                'S' -> MonitorStatusMessage
                else -> null
            }
        }
        routeMessageToSerialiser(MonitorAddMessage::class, MonitorAddMessage)
        routeMessageToSerialiser(MonitorRemoveMessage::class, MonitorRemoveMessage)
        routeMessageToSerialiser(MonitorClearMessage::class, MonitorClearMessage)
        routeMessageToSerialiser(MonitorListMessage::class, MonitorListMessage)
        routeMessageToSerialiser(MonitorStatusMessage::class, MonitorStatusMessage)

        routeCommandToParser("733", RplEndOfMonListMessage)
        routeMessageToSerialiser(RplEndOfMonListMessage::class, RplEndOfMonListMessage)

        routeCommandToParser("732", RplMonListMessage)
        routeMessageToSerialiser(RplMonListMessage::class, RplMonListMessage)

        routeCommandToParser("734", RplMonListIsFullMessage)
        routeMessageToSerialiser(RplMonListIsFullMessage::class, RplMonListIsFullMessage)

        routeCommandToParser("731", RplMonOfflineMessage)
        routeMessageToSerialiser(RplMonOfflineMessage::class, RplMonOfflineMessage)

        routeCommandToParser("730", RplMonOnlineMessage)
        routeMessageToSerialiser(RplMonOnlineMessage::class, RplMonOnlineMessage)

        routeCommandToParser("AUTHENTICATE", AuthenticateMessage)
        routeMessageToSerialiser(AuthenticateMessage::class, AuthenticateMessage)

        routeCommandToParser("ACCOUNT", AccountMessage)
        routeMessageToSerialiser(AccountMessage::class, AccountMessage)

        routeCommandToParser("AWAY", AwayMessage)
        routeMessageToSerialiser(AwayMessage::class, AwayMessage)

        routeCommandToParser("903", Rpl903Message)
        routeMessageToSerialiser(Rpl903Message::class, Rpl903Message)

        routeCommandToParser("904", Rpl904Message)
        routeMessageToSerialiser(Rpl904Message::class, Rpl904Message)

        routeCommandToParser("905", Rpl905Message)
        routeMessageToSerialiser(Rpl905Message::class, Rpl905Message)

        routeCommandToParser("001", Rpl001Message)
        routeMessageToSerialiser(Rpl001Message::class, Rpl001Message)

        routeCommandToParser("002", Rpl002Message)
        routeMessageToSerialiser(Rpl002Message::class, Rpl002Message)

        routeCommandToParser("003", Rpl003Message)
        routeMessageToSerialiser(Rpl003Message::class, Rpl003Message)

        routeCommandToParser("005", Rpl005Message)
        routeMessageToSerialiser(Rpl005Message::class, Rpl005Message)

        routeCommandToParser("331", Rpl331Message)
        routeMessageToSerialiser(Rpl331Message::class, Rpl331Message)

        routeCommandToParser("332", Rpl332Message)
        routeMessageToSerialiser(Rpl332Message::class, Rpl332Message)

        routeCommandToParser("353", Rpl353Message)
        routeMessageToSerialiser(Rpl353Message::class, Rpl353Message)

        routeCommandToParser("372", Rpl372Message)
        routeMessageToSerialiser(Rpl372Message::class, Rpl372Message)

        routeCommandToParser("375", Rpl375Message)
        routeMessageToSerialiser(Rpl375Message::class, Rpl375Message)

        routeCommandToParser("376", Rpl376Message)
        routeMessageToSerialiser(Rpl376Message::class, Rpl376Message)

        routeCommandToParser("422", Rpl422Message)
        routeMessageToSerialiser(Rpl422Message::class, Rpl422Message)

        routeCommandToParser("471", Rpl471Message)
        routeMessageToSerialiser(Rpl471Message::class, Rpl471Message)

        routeCommandToParser("473", Rpl473Message)
        routeMessageToSerialiser(Rpl473Message::class, Rpl473Message)

        routeCommandToParser("474", Rpl474Message)
        routeMessageToSerialiser(Rpl474Message::class, Rpl474Message)

        routeCommandToParser("475", Rpl475Message)
        routeMessageToSerialiser(Rpl475Message::class, Rpl475Message)

        return this
    }

}