package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class MonitorStatusMessage(val subCommand: String = "S"): IMessage {
    override val command: String = "MONITOR"

    companion object Factory: IMessageParser<MonitorStatusMessage>, IMessageSerialiser<MonitorStatusMessage> {

        override fun serialise(message: MonitorStatusMessage): IrcMessage? {
            return IrcMessage(command = message.command, parameters = listOf(message.subCommand))
        }

        override fun parse(message: IrcMessage): MonitorStatusMessage? {
            if (message.parameters.isEmpty()) {
                return null
            }

            return MonitorStatusMessage()
        }
    }

}