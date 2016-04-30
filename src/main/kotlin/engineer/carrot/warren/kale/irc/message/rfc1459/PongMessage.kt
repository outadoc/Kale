package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class PongMessage(val token: String): IMessage {

    companion object Factory: IMessageFactory<PongMessage> {
        override val messageType = PongMessage::class.java
        override val key = "PONG"

        override fun serialise(message: PongMessage): IrcMessage? {
            return IrcMessage(command = key, parameters = listOf(message.token))
        }

        override fun parse(message: IrcMessage): PongMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            return PongMessage(token = message.parameters[0])
        }
    }

}