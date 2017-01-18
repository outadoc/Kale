package chat.willow.kale.irc.message.extension.cap

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class CapNakMessage(val target: String? = null, val caps: List<String>): IMessage {
    override val command: String = "CAP"

    companion object Factory: IMessageParser<CapNakMessage>, IMessageSerialiser<CapNakMessage> {

        override fun serialise(message: CapNakMessage): IrcMessage? {
            val caps = message.caps.joinToString(separator = CharacterCodes.SPACE.toString())

            if (message.target != null) {
                return IrcMessage(command = "CAP", parameters = listOf(message.target, "NAK", caps))
            } else {
                return IrcMessage(command = "CAP", parameters = listOf("NAK", caps))
            }
        }

        override fun parse(message: IrcMessage): CapNakMessage? {
            if (message.parameters.size < 3) {
                return null
            }

            val target = message.parameters[0]
            @Suppress("UNUSED_VARIABLE") val subCommand = message.parameters[1]
            val rawCaps = message.parameters[2]

            val caps = rawCaps.split(delimiters = CharacterCodes.SPACE).filterNot(String::isEmpty)

            return CapNakMessage(target = target, caps = caps)
        }
    }

}