package engineer.carrot.warren.kale.irc.message.ircv3

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.*

data class CapLsMessage(val target: String? = null, val version: String = "302", val caps: Map<String, String?>, val isMultiline: Boolean = false): IMessage {
    override val command: String = "CAP"

    companion object Factory: IMessageParser<CapLsMessage>, IMessageSerialiser<CapLsMessage> {

        override fun serialise(message: CapLsMessage): IrcMessage? {
            val caps = SerialiserHelper.serialiseKeysAndOptionalValues(message.caps, CharacterCodes.EQUALS, CharacterCodes.SPACE)

            if (message.target != null) {
                return IrcMessage(command = "CAP", parameters = listOf(message.target, "LS", message.version, caps))
            } else {
                return IrcMessage(command = "CAP", parameters = listOf("LS", message.version, caps))
            }
        }

        override fun parse(message: IrcMessage): CapLsMessage? {
            if (message.parameters.size < 3) {
                return null
            }

            val target = message.parameters[0]
            val subCommand = message.parameters[1]
            val asteriskOrCaps = message.parameters[2]

            val rawCaps: String
            val isMultiline: Boolean

            if (asteriskOrCaps == "*") {
                rawCaps = message.parameters.getOrNull(3) ?: ""
                isMultiline = true
            } else {
                rawCaps = asteriskOrCaps
                isMultiline = false
            }

            val caps = ParseHelper.parseToKeysAndOptionalValues(rawCaps, CharacterCodes.SPACE, CharacterCodes.EQUALS)

            return CapLsMessage(target = target, caps = caps, isMultiline = isMultiline)
        }
    }

}