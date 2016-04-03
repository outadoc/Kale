package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class TopicMessage(val source: String? = null, val channel: String, val topic: String? = null): IMessage {

    companion object Factory: IMessageFactory<TopicMessage> {
        override val messageType = TopicMessage::class.java
        override val command = "TOPIC"

        override fun serialise(message: TopicMessage): IrcMessage? {
            if (message.topic != null) {
                return IrcMessage(command = TopicMessage.command, prefix = message.source, parameters = listOf(message.channel, message.topic))
            } else {
                return IrcMessage(command = TopicMessage.command, prefix = message.source, parameters = listOf(message.channel))
            }
        }

        override fun parse(message: IrcMessage): TopicMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            val source = message.prefix
            val user = message.parameters[0]
            val topic = message.parameters.getOrNull(1)

            return TopicMessage(source = source, channel = user, topic = topic)
        }
    }

}