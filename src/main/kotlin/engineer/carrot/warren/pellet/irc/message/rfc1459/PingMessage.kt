package engineer.carrot.warren.pellet.irc.message.rfc1459

import engineer.carrot.warren.pellet.irc.message.IMessage
import engineer.carrot.warren.pellet.irc.message.IMessageFactory
import engineer.carrot.warren.pellet.irc.message.IrcMessage

data class PingMessage(val token: String): IMessage {

    companion object Factory: IMessageFactory<PingMessage> {
        override val messageType = PingMessage::class.java
        override val command = "PING"

        override fun serialise(messageOne: PingMessage): IrcMessage? {
            return IrcMessage(command = PingMessage.command, parameters = listOf(messageOne.token))
        }

        override fun parse(message: IrcMessage): PingMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            return PingMessage(token = message.parameters[0])
        }
    }

}