package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

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