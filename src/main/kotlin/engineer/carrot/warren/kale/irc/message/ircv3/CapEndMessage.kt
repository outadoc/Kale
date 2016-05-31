package engineer.carrot.warren.kale.irc.message.ircv3

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class CapEndMessage(val target: String? = null): IMessage {
    override val command: String = "CAP"

    companion object Factory: IMessageParser<CapEndMessage>, IMessageSerialiser<CapEndMessage> {

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