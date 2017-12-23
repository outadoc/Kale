package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.generator.message.*
import chat.willow.kale.irc.CharacterCodes

object Rpl353Message : ICommand {

    override val command = "353"

    data class Message(val source: String, val target: String, val visibility: String, val channel: String, val names: List<String>) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.size < 4) {
                    return null
                }

                val source = components.prefix ?: ""
                val target = components.parameters[0]
                val visibility = components.parameters[1]
                val channel = components.parameters[2]
                val names = components.parameters[3].split(delimiters = CharacterCodes.SPACE).filterNot(String::isEmpty)

                return Message(source, target, visibility, channel, names)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val names = message.names.joinToString(separator = CharacterCodes.SPACE.toString())

                return IrcMessageComponents(prefix = message.source, parameters = listOf(message.target, message.visibility, message.channel, names))
            }

        }

    }

}