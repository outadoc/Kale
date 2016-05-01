package engineer.carrot.warren.kale.irc.message.ircv3

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class CapEndMessage(val target: String? = null): IMessage {

    companion object Factory: IMessageFactory<CapEndMessage> {
        override val messageType = CapEndMessage::class.java
        override val key = "CAPEND"

        override fun serialise(message: CapEndMessage): IrcMessage? {
            if (message.target != null) {
                return IrcMessage(command = "CAP", parameters = listOf(message.target, "END"))
            } else {
                return IrcMessage(command = "CAP", parameters = listOf("END"))
            }
        }

        override fun parse(message: IrcMessage): CapEndMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val target = message.parameters[0]
            val subCommand = message.parameters[1]

            return CapEndMessage(target = target)
        }
    }

}