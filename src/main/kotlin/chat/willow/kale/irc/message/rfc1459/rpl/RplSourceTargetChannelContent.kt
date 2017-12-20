package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.KaleDescriptor
import chat.willow.kale.commandMatcher
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser

object RplSourceTargetChannelContent {

    open class Message(val source: String, val target: String, val channel: String, val content: String) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Message) return false

            if (source != other.source) return false
            if (target != other.target) return false
            if (channel != other.channel) return false
            if (content != other.content) return false

            return true
        }

        override fun hashCode(): Int {
            var result = source.hashCode()
            result = 31 * result + target.hashCode()
            result = 31 * result + channel.hashCode()
            result = 31 * result + content.hashCode()
            return result
        }
    }

    abstract class Parser(val command: String) : MessageParser<Message>() {

        override fun parseFromComponents(components: IrcMessageComponents): Message? {
            if (components.parameters.size < 3) {
                return null
            }

            val source = components.prefix ?: ""
            val target = components.parameters[0]
            val channel = components.parameters[1]
            val content = components.parameters[2]

            return Message(source, target, channel, content)
        }
    }

    abstract class Serialiser(val command: String) : MessageSerialiser<Message>(command) {

        override fun serialiseToComponents(message: Message): IrcMessageComponents {
            return IrcMessageComponents(prefix = message.source, parameters = listOf(message.target, message.channel, message.content))
        }

    }

    abstract class Descriptor(command: String, parser: IMessageParser<Message>) : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = parser)

}