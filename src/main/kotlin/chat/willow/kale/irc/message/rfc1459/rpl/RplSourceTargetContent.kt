package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser

object RplSourceTargetContent {

    data class Message(val source: String, val target: String, val contents: String)

    abstract class Parser(val command: String) : MessageParser<Message>(command) {

        override fun parseFromComponents(components: IrcMessageComponents): Message? {
            if (components.parameters.size < 2) {
                return null
            }

            val source = components.prefix ?: ""
            val target = components.parameters[0]
            val contents = components.parameters[1]

            return Message(source = source, target = target, contents = contents)
        }
    }

    abstract class Serialiser(val command: String) : MessageSerialiser<Message>(command) {

        override fun serialiseToComponents(message: Message): IrcMessageComponents {
            return IrcMessageComponents(prefix = message.source, parameters = listOf(message.target, message.contents))
        }

    }

}