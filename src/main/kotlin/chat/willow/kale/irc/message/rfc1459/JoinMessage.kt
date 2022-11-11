package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.core.ICommand
import chat.willow.kale.core.message.*
import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object JoinMessage : ICommand {

    override val command = "JOIN"

    data class Command(val channels: List<String>, val keys: List<String>? = null) {

        object Descriptor : KaleDescriptor<Command>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Command>() {

            override fun parseFromComponents(components: IrcMessageComponents): Command? {
                if (components.parameters.isEmpty()) {
                    return null
                }

                val unsplitChannels = components.parameters[0]
                val channels = unsplitChannels.split(CharacterCodes.COMMA).filterNot(String::isEmpty)

                return if (components.parameters.size < 2) {
                    Command(channels)
                } else {
                    val unsplitKeys = components.parameters[1]
                    val keys = unsplitKeys.split(CharacterCodes.COMMA).filterNot(String::isEmpty)

                    Command(channels, keys)
                }
            }
        }

        object Serialiser : MessageSerialiser<Command>(command) {

            override fun serialiseToComponents(message: Command): IrcMessageComponents {
                val channels = message.channels.joinToString(separator = CharacterCodes.COMMA.toString())

                return if (message.keys == null || message.keys.isEmpty()) {
                    IrcMessageComponents(parameters = listOf(channels))
                } else {
                    val keys = message.keys.joinToString(separator = CharacterCodes.COMMA.toString())

                    IrcMessageComponents(parameters = listOf(channels, keys))
                }
            }
        }

    }

    data class Message(val source: Prefix, val channels: List<String>) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.isEmpty()) {
                    return null
                }

                val source = PrefixParser.parse(components.prefix ?: "") ?: return null
                val unsplitChannels = components.parameters[0]
                val channels = unsplitChannels.split(CharacterCodes.COMMA).filterNot(String::isEmpty)

                return Message(source, channels)
            }
        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val prefix = PrefixSerialiser.serialise(message.source)
                val channels = message.channels.joinToString(separator = CharacterCodes.COMMA.toString())

                return IrcMessageComponents(prefix = prefix, parameters = listOf(channels))
            }
        }

    }

}