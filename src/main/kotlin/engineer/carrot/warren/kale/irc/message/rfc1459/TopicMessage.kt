package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

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
            if (message.parameters.size < 1) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val user = message.parameters[0]
            val topic = message.parameters.getOrNull(1)

            return TopicMessage(source = source, channel = user, topic = topic)
        }
    }

}