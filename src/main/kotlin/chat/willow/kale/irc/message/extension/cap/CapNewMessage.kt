package chat.willow.kale.irc.message.extension.cap

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.*

data class CapNewMessage(val target: String, val caps: Map<String, String?>): IMessage {
    override val command: String = "CAP"

    companion object Factory: IMessageParser<CapNewMessage>, IMessageSerialiser<CapNewMessage> {

        override fun serialise(message: CapNewMessage): IrcMessage? {
            val caps = SerialiserHelper.serialiseKeysAndOptionalValues(message.caps, CharacterCodes.EQUALS, CharacterCodes.SPACE)

            return IrcMessage(command = message.command, parameters = listOf(message.target, "NEW", caps))
        }

        override fun parse(message: IrcMessage): CapNewMessage? {
            if (message.parameters.size < 3) {
                return null
            }

            val target = message.parameters[0]
            @Suppress("UNUSED_VARIABLE") val subCommand = message.parameters[1]
            val rawCaps = message.parameters[2]

            val caps = ParseHelper.parseToKeysAndOptionalValues(rawCaps, CharacterCodes.SPACE, CharacterCodes.EQUALS)

            return CapNewMessage(target = target, caps = caps)
        }
    }

}