package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class PongMessage(val token: String): IMessage {
    override val command: String = "PONG"

    companion object Factory: IMessageParser<PongMessage>, IMessageSerialiser<PongMessage> {

        override fun serialise(message: PongMessage): IrcMessage? {
            return IrcMessage(command = message.command, parameters = listOf(message.token))
        }

        override fun parse(message: IrcMessage): PongMessage? {
            if (message.parameters.isEmpty()) {
                return null
            }

            return PongMessage(token = message.parameters[0])
        }
    }

}