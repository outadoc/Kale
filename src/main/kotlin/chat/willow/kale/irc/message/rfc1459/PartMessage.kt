package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.ICommand
import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.KaleDescriptor
import chat.willow.kale.commandMatcher
import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object PartMessage : ICommand {
    
    override val command = "PART"
    
    data class Command(val channels: List<String>) {

        object Descriptor : KaleDescriptor<Command>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Command>() {

            override fun parseFromComponents(components: IrcMessageComponents): Command? {
                if (components.parameters.isEmpty()) {
                    return null
                }

                val unsplitChannels = components.parameters[0]
                val channels = unsplitChannels.split(delimiters = CharacterCodes.COMMA)

                return Command(channels)
            }
            
        }
        
        object Serialiser : MessageSerialiser<Command>(command) {

            override fun serialiseToComponents(message: Command): IrcMessageComponents {
                val channels = message.channels.joinToString(separator = CharacterCodes.COMMA.toString())

                return IrcMessageComponents(parameters = listOf(channels))
            }
            
        }
        
    }
    
    data class Message(val source: Prefix, val channels: List<String>) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.isEmpty() || components.prefix == null) {
                    return null
                }

                val source = PrefixParser.parse(components.prefix) ?: return null
                val unsplitChannels = components.parameters[0]
                val channels = unsplitChannels.split(delimiters = CharacterCodes.COMMA)

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