package engineer.carrot.warren.pellet.irc.message.rfc1459

import engineer.carrot.warren.pellet.irc.message.IMessage
import engineer.carrot.warren.pellet.irc.message.IMessageFactory
import engineer.carrot.warren.pellet.irc.message.IrcMessage

data class PongMessage(val token: String): IMessage {

    companion object Factory: IMessageFactory<PongMessage> {
        override val messageType = PongMessage::class.java
        override val command = "PONG"

        override fun serialise(messageOne: PongMessage): IrcMessage? {
            return IrcMessage(command = command, parameters = listOf(messageOne.token))
        }

        override fun parse(message: IrcMessage): PongMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            return PongMessage(token = message.parameters[0])
        }
    }

}