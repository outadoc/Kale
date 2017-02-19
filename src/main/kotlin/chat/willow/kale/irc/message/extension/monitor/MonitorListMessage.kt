package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class MonitorListMessage(val subCommand: String = "L"): IMessage {
    override val command: String = "MONITOR"

    companion object Factory: IMessageParser<MonitorListMessage>, IMessageSerialiser<MonitorListMessage> {

        override fun serialise(message: MonitorListMessage): IrcMessage? {
            return IrcMessage(command = message.command, parameters = listOf(message.subCommand))
        }

        override fun parse(message: IrcMessage): MonitorListMessage? {
            if (message.parameters.isEmpty()) {
                return null
            }

            return MonitorListMessage()
        }
    }

}