package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand
import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.KaleDescriptor
import chat.willow.kale.commandMatcher
import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser

object Rpl005Message : ICommand {

    override val command = "005"

    data class Message(val source: String, val target: String, val tokens: Map<String, String?>) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.size < 2) {
                    return null
                }

                val source = components.prefix ?: ""
                val target = components.parameters[0]

                val tokens = mutableMapOf<String, String?>()

                for (i in 1 until components.parameters.size) {
                    val token = components.parameters[i].split(delimiters = CharacterCodes.EQUALS, limit = 2)

                    if (token.isEmpty() || token[0].isEmpty()) {
                        continue
                    }

                    var value = token.getOrNull(1)
                    if (value != null && value.isEmpty()) {
                        value = null
                    }

                    tokens[token[0]] = value
                }

                return Message(source, target, tokens)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val tokens = mutableListOf<String>()

                for ((key, value) in message.tokens) {
                    if (value.isNullOrEmpty()) {
                        tokens.add(key)
                    } else {
                        tokens.add("$key${CharacterCodes.EQUALS}$value")
                    }
                }

                val parameters = listOf(message.target) + tokens

                return IrcMessageComponents(prefix = message.source, parameters = parameters)
            }

        }

    }

}