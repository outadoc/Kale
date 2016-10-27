package engineer.carrot.warren.kale.irc.message.extension.cap

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.*

data class CapDelMessage(val target: String, val caps: List<String>): IMessage {
    override val command: String = "CAP"

    companion object Factory: IMessageParser<CapDelMessage>, IMessageSerialiser<CapDelMessage> {

        override fun serialise(message: CapDelMessage): IrcMessage? {
            val caps = SerialiserHelper.serialiseKeysAndOptionalValues(message.caps.associate { (it to null) }, CharacterCodes.EQUALS, CharacterCodes.SPACE)

            return IrcMessage(command = message.command, parameters = listOf(message.target, "DEL", caps))
        }

        override fun parse(message: IrcMessage): CapDelMessage? {
            if (message.parameters.size < 3) {
                return null
            }

            val target = message.parameters[0]
            @Suppress("UNUSED_VARIABLE") val subCommand = message.parameters[1]
            val rawCaps = message.parameters[2]

            val caps = ParseHelper.parseToKeysAndOptionalValues(rawCaps, CharacterCodes.SPACE, CharacterCodes.EQUALS).keys.toList()

            return CapDelMessage(target = target, caps = caps)
        }
    }

}