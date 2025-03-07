package chat.willow.kale

import chat.willow.kale.core.message.IMessageSerialiser
import chat.willow.kale.irc.message.extension.cap.CapMessage
import chat.willow.kale.irc.message.extension.monitor.MonitorMessage
import chat.willow.kale.irc.message.extension.sasl.AuthenticateMessage
import chat.willow.kale.irc.message.rfc1459.*
import chat.willow.kale.irc.message.utility.RawMessage
import kotlin.reflect.KClass

interface IKaleRouter {

    fun <M: Any> register(messageClass: KClass<M>, serialiser: IMessageSerialiser<M>)
    fun <M: Any> serialiserFor(messageClass: KClass<M>): IMessageSerialiser<M>?

}

open class KaleRouter : IKaleRouter {

    private val messagesToSerialisers = hashMapOf<KClass<*>, IMessageSerialiser<*>>()

    override fun <M : Any> register(messageClass: KClass<M>, serialiser: IMessageSerialiser<M>) {
        messagesToSerialisers[messageClass] = serialiser
    }

    override fun <M : Any> serialiserFor(messageClass: KClass<M>): IMessageSerialiser<M>? {
        @Suppress("UNCHECKED_CAST")
        return messagesToSerialisers[messageClass] as? IMessageSerialiser<M>
    }

}

class KaleClientRouter : KaleRouter() {

    init {
        register(RawMessage.Line::class, RawMessage.Line.Serialiser)
        register(PingMessage.Command::class, PingMessage.Command.Serialiser)
        register(PongMessage.Message::class, PongMessage.Message.Serialiser)
        register(NickMessage.Command::class, NickMessage.Command.Serialiser)
        register(QuitMessage.Command::class, QuitMessage.Command.Serialiser)
        register(PartMessage.Command::class, PartMessage.Command.Serialiser)
        register(ModeMessage.Command::class, ModeMessage.Command.Serialiser)
        register(PrivMsgMessage.Command::class, PrivMsgMessage.Command.Serialiser)
        register(NoticeMessage.Message::class, NoticeMessage.Message.Serialiser)
        register(InviteMessage.Command::class, InviteMessage.Command.Serialiser)
        register(TopicMessage.Command::class, TopicMessage.Command.Serialiser)
        register(KickMessage.Command::class, KickMessage.Command.Serialiser)
        register(JoinMessage.Command::class, JoinMessage.Command.Serialiser)
        register(UserMessage.Command::class, UserMessage.Command.Serialiser)
        register(CapMessage.Ls.Command::class, CapMessage.Ls.Command.Serialiser)
        register(CapMessage.Ack.Command::class, CapMessage.Ack.Command.Serialiser)
        register(CapMessage.End.Command::class, CapMessage.End.Command.Serialiser)
        register(CapMessage.Req.Command::class, CapMessage.Req.Command.Serialiser)
        register(MonitorMessage.Add.Command::class, MonitorMessage.Add.Command.Serialiser)
        register(MonitorMessage.Remove.Command::class, MonitorMessage.Remove.Command.Serialiser)
        register(MonitorMessage.Status.Command::class, MonitorMessage.Status.Command.Serialiser)
        register(MonitorMessage.ListAll.Command::class, MonitorMessage.ListAll.Command.Serialiser)
        register(MonitorMessage.Clear.Command::class, MonitorMessage.Clear.Command.Serialiser)
        register(AuthenticateMessage.Command::class, AuthenticateMessage.Command.Serialiser)
    }

}