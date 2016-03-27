package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class PrivMsgMessage(val source: String? = null, val target: String, val message: String): IMessage {

    companion object Factory: IMessageFactory<PrivMsgMessage> {
        override val messageType = PrivMsgMessage::class.java
        override val command = "PRIVMSG"

        override fun serialise(message: PrivMsgMessage): IrcMessage? {
            return IrcMessage(command = command, prefix = message.source, parameters = listOf(message.target, message.message))
        }

        override fun parse(message: IrcMessage): PrivMsgMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val target = message.parameters[0]
            val privMessage = message.parameters[1]

            return PrivMsgMessage(source = message.prefix, target = target, message = privMessage)
        }
    }

}