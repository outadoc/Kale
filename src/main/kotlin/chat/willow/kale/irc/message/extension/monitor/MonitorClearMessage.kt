package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class MonitorClearMessage(val subCommand: String = "C"): IMessage {
    override val command: String = "MONITOR"

    companion object Factory: IMessageParser<MonitorClearMessage>, IMessageSerialiser<MonitorClearMessage> {

        override fun serialise(message: MonitorClearMessage): IrcMessage? {
            return IrcMessage(command = message.command, parameters = listOf(message.subCommand))
        }

        override fun parse(message: IrcMessage): MonitorClearMessage? {
            if (message.parameters.isEmpty()) {
                return null
            }

            return MonitorClearMessage()
        }
    }

}