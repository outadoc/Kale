package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class PingMessage(val token: String): IMessage {
    override val command: String = "PING"

    companion object Factory: IMessageParser<PingMessage>, IMessageSerialiser<PingMessage> {

        override fun serialise(message: PingMessage): IrcMessage? {
            return IrcMessage(command = message.command, parameters = listOf(message.token))
        }

        override fun parse(message: IrcMessage): PingMessage? {
            if (message.parameters.isEmpty()) {
                return null
            }

            return PingMessage(token = message.parameters[0])
        }
    }

}