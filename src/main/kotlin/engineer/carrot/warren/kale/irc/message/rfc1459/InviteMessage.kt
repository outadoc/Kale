package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class InviteMessage(val source: String? = null, val user: String, val channel: String): IMessage {

    companion object Factory: IMessageFactory<InviteMessage> {
        override val messageType = InviteMessage::class.java
        override val command = "INVITE"

        override fun serialise(message: InviteMessage): IrcMessage? {
            return IrcMessage(command = InviteMessage.command, prefix = message.source, parameters = listOf(message.user, message.channel))
        }

        override fun parse(message: IrcMessage): InviteMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix
            val user = message.parameters[0]
            val channel = message.parameters[1]

            return InviteMessage(source = source, user = user, channel = channel)
        }
    }

}