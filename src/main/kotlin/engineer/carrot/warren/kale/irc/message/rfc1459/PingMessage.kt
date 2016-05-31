package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class PingMessage(val token: String): IMessage {
    override val command: String = "PING"

    companion object Factory: IMessageParser<PingMessage>, IMessageSerialiser<PingMessage> {

        override fun serialise(message: PingMessage): IrcMessage? {
            return IrcMessage(command = message.command, parameters = listOf(message.token))
        }

        override fun parse(message: IrcMessage): PingMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            return PingMessage(token = message.parameters[0])
        }
    }

}