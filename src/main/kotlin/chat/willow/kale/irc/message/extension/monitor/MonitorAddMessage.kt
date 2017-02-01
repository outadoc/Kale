package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class MonitorAddMessage(val targets: List<String>): IMessage {
    override val command: String = "MONITOR"

    companion object Factory: IMessageParser<MonitorAddMessage>, IMessageSerialiser<MonitorAddMessage> {

        override fun serialise(message: MonitorAddMessage): IrcMessage? {
            val targets = message.targets.joinToString(separator = CharacterCodes.COMMA.toString())

            return IrcMessage(command = message.command, parameters = listOf(CharacterCodes.PLUS.toString(), targets))
        }

        override fun parse(message: IrcMessage): MonitorAddMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            @Suppress("UNUSED_VARIABLE") val plus = message.parameters[0]
            val rawTargets = message.parameters[1]

            val targets = rawTargets.split(delimiters = CharacterCodes.COMMA)

            return MonitorAddMessage(targets = targets)
        }
    }

}