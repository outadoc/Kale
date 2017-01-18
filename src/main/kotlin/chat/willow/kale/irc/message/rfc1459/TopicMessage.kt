package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

data class TopicMessage(val source: Prefix? = null, val channel: String, val topic: String? = null): IMessage {
    override val command: String = "TOPIC"

    companion object Factory: IMessageParser<TopicMessage>, IMessageSerialiser<TopicMessage> {

        override fun serialise(message: TopicMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }

            if (message.topic != null) {
                return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.channel, message.topic))
            } else {
                return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.channel))
            }
        }

        override fun parse(message: IrcMessage): TopicMessage? {
            if (message.parameters.isEmpty()) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val user = message.parameters[0]
            val topic = message.parameters.getOrNull(1)

            return TopicMessage(source = source, channel = user, topic = topic)
        }
    }

}