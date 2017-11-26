package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser

abstract class RplSourceTargetChannelContent {

    data class Message(val source: String, val target: String, val channel: String, val content: String)

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

}