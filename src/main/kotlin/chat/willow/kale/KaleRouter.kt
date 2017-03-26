package chat.willow.kale

import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.extension.account_notify.AccountMessage
import chat.willow.kale.irc.message.extension.away_notify.AwayMessage
import chat.willow.kale.irc.message.extension.batch.BatchMessage
import chat.willow.kale.irc.message.extension.cap.CapMessage
import chat.willow.kale.irc.message.extension.chghost.ChgHostMessage
import chat.willow.kale.irc.message.extension.monitor.MonitorMessage
import chat.willow.kale.irc.message.extension.monitor.rpl.*
import chat.willow.kale.irc.message.extension.sasl.AuthenticateMessage
import chat.willow.kale.irc.message.extension.sasl.rpl.Rpl903Message
import chat.willow.kale.irc.message.extension.sasl.rpl.Rpl904Message
import chat.willow.kale.irc.message.extension.sasl.rpl.Rpl905Message
import chat.willow.kale.irc.message.rfc1459.*
import chat.willow.kale.irc.message.rfc1459.rpl.*
import chat.willow.kale.irc.message.utility.RawMessage
import kotlin.reflect.KClass

interface IKaleRouter {

    fun register(command: String, handler: IKaleIrcMessageHandler)
    fun unregister(command: String)
    fun handlerFor(command: String): IKaleIrcMessageHandler?

    fun <M: Any> register(messageClass: KClass<M>, serialiser: IMessageSerialiser<M>)
    fun <M: Any> serialiserFor(messageClass: Class<M>): IMessageSerialiser<M>?

}

class KaleRouter : IKaleRouter {

    private val commandsToParsers = hashMapOf<String, IKaleIrcMessageHandler>()
    private val messagesToSerialisers = hashMapOf<Class<*>, IMessageSerialiser<*>>()

    override fun register(command: String, handler: IKaleIrcMessageHandler) {
        commandsToParsers[command] = handler
    }

    override fun unregister(command: String) {
        commandsToParsers -= command
    }

    override fun handlerFor(command: String): IKaleIrcMessageHandler? {
        return commandsToParsers[command]
    }

    override fun <M : Any> register(messageClass: KClass<M>, serialiser: IMessageSerialiser<M>) {
        messagesToSerialisers[messageClass.java] = serialiser
    }

    override fun <M: Any> serialiserFor(messageClass: Class<M>): IMessageSerialiser<M>? {
        @Suppress("UNCHECKED_CAST")
        return messagesToSerialisers[messageClass] as? IMessageSerialiser<M>
    }

    fun useClientDefaults(): KaleRouter {
        register(RawMessage.Line::class, RawMessage.Line.Serialiser)

        commandsToParsers["PING"] = KaleParseOnlyHandler(PingMessage.Command.Parser)
        register(PingMessage.Command::class, PingMessage.Command.Serialiser)

        commandsToParsers["PONG"] = KaleParseOnlyHandler(PongMessage.Message.Parser)
        register(PongMessage.Message::class, PongMessage.Message.Serialiser)

        commandsToParsers["NICK"] = KaleParseOnlyHandler(NickMessage.Message.Parser)
        register(NickMessage.Command::class, NickMessage.Command.Serialiser)

        commandsToParsers["QUIT"] = KaleParseOnlyHandler(QuitMessage.Message.Parser)
        register(QuitMessage.Command::class, QuitMessage.Command.Serialiser)

        commandsToParsers["PART"] = KaleParseOnlyHandler(PartMessage.Message.Parser)
        register(PartMessage.Command::class, PartMessage.Command.Serialiser)

        commandsToParsers["MODE"] = KaleParseOnlyHandler(ModeMessage.Message.Parser)
        register(ModeMessage.Command::class, ModeMessage.Command.Serialiser)

        commandsToParsers["PRIVMSG"] = KaleParseOnlyHandler(PrivMsgMessage.Message.Parser)
        register(PrivMsgMessage.Command::class, PrivMsgMessage.Command.Serialiser)

        commandsToParsers["NOTICE"] = KaleParseOnlyHandler(NoticeMessage.Message.Parser)
        register(NoticeMessage.Message::class, NoticeMessage.Message.Serialiser)

        commandsToParsers["INVITE"] = KaleParseOnlyHandler(InviteMessage.Message.Parser)
        register(InviteMessage.Command::class, InviteMessage.Command.Serialiser)

        commandsToParsers["TOPIC"] = KaleParseOnlyHandler(TopicMessage.Message.Parser)
        register(TopicMessage.Command::class, TopicMessage.Command.Serialiser)

        commandsToParsers["KICK"] = KaleParseOnlyHandler(KickMessage.Message.Parser)
        register(KickMessage.Command::class, KickMessage.Command.Serialiser)

        commandsToParsers["JOIN"] = KaleParseOnlyHandler(JoinMessage.Message.Parser)
        register(JoinMessage.Command::class, JoinMessage.Command.Serialiser)

        val capHandlers = mapOf(
                CapMessage.Ls.subcommand to KaleParseOnlyHandler(CapMessage.Ls.Message.Parser),
                CapMessage.Ack.subcommand to KaleParseOnlyHandler(CapMessage.Ack.Message.Parser),
                CapMessage.Del.subcommand to KaleParseOnlyHandler(CapMessage.Del.Message.Parser),
                CapMessage.Nak.subcommand to KaleParseOnlyHandler(CapMessage.Nak.Message.Parser),
                CapMessage.New.subcommand to KaleParseOnlyHandler(CapMessage.New.Message.Parser))
        register(CapMessage.command, KaleSubcommandHandler(capHandlers, subcommandPosition = 1))
        register(CapMessage.Ls.Command::class, CapMessage.Ls.Command.Serialiser)
        register(CapMessage.Ack.Command::class, CapMessage.Ack.Command.Serialiser)
        register(CapMessage.End.Command::class, CapMessage.End.Command.Serialiser)
        register(CapMessage.Req.Command::class, CapMessage.Req.Command.Serialiser)

        // TODO: use special subcommand handler for +-
        val batchHandlers = mapOf(
                BatchMessage.Start.subcommand to KaleParseOnlyHandler(BatchMessage.Start.Message.Parser),
                BatchMessage.End.subcommand to KaleParseOnlyHandler(BatchMessage.End.Message.Parser))
        register(BatchMessage.command, KaleSubcommandHandler(batchHandlers))

        register(MonitorMessage.Add.Command::class, MonitorMessage.Add.Command.Serialiser)
        register(MonitorMessage.Remove.Command::class, MonitorMessage.Remove.Command.Serialiser)
        register(MonitorMessage.Status.Command::class, MonitorMessage.Status.Command.Serialiser)
        register(MonitorMessage.ListAll.Command::class, MonitorMessage.ListAll.Command.Serialiser)
        register(MonitorMessage.Clear.Command::class, MonitorMessage.Clear.Command.Serialiser)

        commandsToParsers[RplEndOfMonList.command] = KaleParseOnlyHandler(RplEndOfMonList.Message.Parser)

        commandsToParsers[RplMonList.command] = KaleParseOnlyHandler(RplMonList.Message.Parser)

        commandsToParsers[RplMonListIsFull.command] = KaleParseOnlyHandler(RplMonListIsFull.Message.Parser)

        commandsToParsers[RplMonOffline.command] = KaleParseOnlyHandler(RplMonOffline.Message.Parser)

        commandsToParsers[RplMonOnline.command] = KaleParseOnlyHandler(RplMonOnline.Message.Parser)

        commandsToParsers[AuthenticateMessage.command] = KaleParseOnlyHandler(AuthenticateMessage.Message.Parser)
        register(AuthenticateMessage.Command::class, AuthenticateMessage.Command.Serialiser)

        commandsToParsers[AccountMessage.command] = KaleParseOnlyHandler(AccountMessage.Message.Parser)

        commandsToParsers[AwayMessage.command] = KaleParseOnlyHandler(AwayMessage.Message.Parser)

        commandsToParsers[ChgHostMessage.command] = KaleParseOnlyHandler(ChgHostMessage.Message.Parser)

        commandsToParsers[Rpl903Message.command] = KaleParseOnlyHandler(Rpl903Message.Parser)

        commandsToParsers[Rpl904Message.command] = KaleParseOnlyHandler(Rpl904Message.Parser)

        commandsToParsers[Rpl905Message.command] = KaleParseOnlyHandler(Rpl905Message.Parser)

        commandsToParsers[Rpl001Message.command] = KaleParseOnlyHandler(Rpl001Message.Parser)

        commandsToParsers[Rpl002Message.command] = KaleParseOnlyHandler(Rpl002Message.Parser)

        commandsToParsers[Rpl003Message.command] = KaleParseOnlyHandler(Rpl003Message.Parser)

        commandsToParsers[Rpl005Message.command] = KaleParseOnlyHandler(Rpl005Message.Message.Parser)

        commandsToParsers[Rpl331Message.command] = KaleParseOnlyHandler(Rpl331Message.Parser)

        commandsToParsers[Rpl332Message.command] = KaleParseOnlyHandler(Rpl332Message.Parser)

        commandsToParsers[Rpl353Message.command] = KaleParseOnlyHandler(Rpl353Message.Message.Parser)

        commandsToParsers[Rpl372Message.command] = KaleParseOnlyHandler(Rpl372Message.Parser)

        commandsToParsers[Rpl375Message.command] = KaleParseOnlyHandler(Rpl375Message.Parser)

        commandsToParsers[Rpl376Message.command] = KaleParseOnlyHandler(Rpl376Message.Parser)

        commandsToParsers[Rpl422Message.command] = KaleParseOnlyHandler(Rpl422Message.Parser)

        commandsToParsers[Rpl471Message.command] = KaleParseOnlyHandler(Rpl471Message.Parser)

        commandsToParsers[Rpl473Message.command] = KaleParseOnlyHandler(Rpl473Message.Parser)

        commandsToParsers[Rpl474Message.command] = KaleParseOnlyHandler(Rpl474Message.Parser)

        commandsToParsers[Rpl475Message.command] = KaleParseOnlyHandler(Rpl475Message.Parser)

        return this
    }

}