package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl332Message(val source: String, val target: String, val channel: String, val topic: String): IMessage {

    companion object Factory: IMessageFactory<Rpl332Message> {
        override val messageType = Rpl332Message::class.java
        override val key = "332"

        override fun serialise(message: Rpl332Message): IrcMessage? {
            return IrcMessage(command = key, prefix = message.source, parameters = listOf(message.target, message.channel, message.topic))
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