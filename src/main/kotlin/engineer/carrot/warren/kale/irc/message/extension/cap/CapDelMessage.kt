package engineer.carrot.warren.kale.irc.message.extension.cap

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class CapDelMessage(val target: String, val caps: List<String>): IMessage {
    override val command: String = "CAP"

    companion object Factory: IMessageParser<CapDelMessage>, IMessageSerialiser<CapDelMessage> {

        override fun serialise(message: CapDelMessage): IrcMessage? {
            val caps = message.caps.joinToString(separator = " ")

            return IrcMessage(command = message.command, parameters = listOf(message.target, "DEL", caps))
        }

        override fun parse(message: IrcMessage): CapDelMessage? {
            if (message.parameters.size < 3) {
                return null
            }

            val target = message.parameters[0]
            @Suppress("UNUSED_VARIABLE") val subCommand = message.parameters[1]
            val rawCaps = message.parameters[2]

            val caps = rawCaps.split(delimiters = CharacterCodes.SPACE).filterNot(String::isEmpty)

            return CapDelMessage(target = target, caps = caps)
        }
    }

}