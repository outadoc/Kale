package chat.willow.kale.irc.message.extension.away_notify

import chat.willow.kale.generator.message.*
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object AwayMessage : ICommand {

    override val command = "AWAY"

    data class Message(val source: Prefix, val message: String?) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                val prefix = components.prefix ?: return null

                val source = PrefixParser.parse(prefix) ?: return null
                val awayMessage = components.parameters.getOrNull(0)

                return Message(source, awayMessage)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val prefix = PrefixSerialiser.serialise(message.source)
                val awayMessage = message.message

                return if (awayMessage == null) {
                    IrcMessageComponents(prefix = prefix, parameters = listOf())
                } else {
                    IrcMessageComponents(prefix = prefix, parameters = listOf(awayMessage))
                }
            }

        }

    }

}