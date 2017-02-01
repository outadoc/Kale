package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class MonitorRemoveMessage(val targets: List<String>): IMessage {
    override val command: String = "MONITOR"

    companion object Factory: IMessageParser<MonitorRemoveMessage>, IMessageSerialiser<MonitorRemoveMessage> {

        override fun serialise(message: MonitorRemoveMessage): IrcMessage? {
            val targets = message.targets.joinToString(separator = CharacterCodes.COMMA.toString())

            return IrcMessage(command = message.command, parameters = listOf(CharacterCodes.MINUS.toString(), targets))
        }

        override fun parse(message: IrcMessage): MonitorRemoveMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            @Suppress("UNUSED_VARIABLE") val minus = message.parameters[0]
            val rawTargets = message.parameters[1]

            val targets = rawTargets.split(delimiters = CharacterCodes.COMMA)

            return MonitorRemoveMessage(targets = targets)
        }
    }

}