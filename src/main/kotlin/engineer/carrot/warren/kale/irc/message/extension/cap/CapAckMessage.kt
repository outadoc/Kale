package engineer.carrot.warren.kale.irc.message.extension.cap

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class CapAckMessage(val target: String? = null, val caps: List<String>): IMessage {
    override val command: String = "CAP"

    companion object Factory: IMessageParser<CapAckMessage>, IMessageSerialiser<CapAckMessage> {

        override fun serialise(message: CapAckMessage): IrcMessage? {
            val caps = message.caps.joinToString(separator = " ")

            if (message.target != null) {
                return IrcMessage(command = message.command, parameters = listOf(message.target, "ACK", caps))
            } else {
                return IrcMessage(command = message.command, parameters = listOf("ACK", caps))
            }
        }

        override fun parse(message: IrcMessage): CapAckMessage? {
            if (message.parameters.size < 3) {
                return null
            }

            val target = message.parameters[0]
            @Suppress("UNUSED_VARIABLE") val subCommand = message.parameters[1]
            val rawCaps = message.parameters[2]

            val caps = rawCaps.split(delimiters = CharacterCodes.SPACE).filterNot(String::isEmpty)

            return CapAckMessage(target = target, caps = caps)
        }
    }

}