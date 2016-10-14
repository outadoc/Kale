package engineer.carrot.warren.kale.irc.message.rfc1459.rpl

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl332Message(val source: String, val target: String, val channel: String, val topic: String): IMessage {
    override val command: String = "332"

    companion object Factory: IMessageParser<Rpl332Message>, IMessageSerialiser<Rpl332Message> {

        override fun serialise(message: Rpl332Message): IrcMessage? {
            return IrcMessage(command = message.command, prefix = message.source, parameters = listOf(message.target, message.channel, message.topic))
        }

        override fun parse(message: IrcMessage): Rpl332Message? {
            if (message.parameters.size < 3) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val channel = message.parameters[1]
            val topic = message.parameters[2]

            return Rpl332Message(source = source, channel = channel, target = target, topic = topic)
        }
    }
}