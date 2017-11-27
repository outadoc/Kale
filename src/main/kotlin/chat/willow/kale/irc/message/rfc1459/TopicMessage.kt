package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.ICommand
import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.KaleDescriptor
import chat.willow.kale.commandMatcher
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object TopicMessage : ICommand {

    override val command = "TOPIC"

    data class Command(val channel: String, val topic: String? = null) {

        object Descriptor : KaleDescriptor<Command>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Command>() {
            
            override fun parseFromComponents(components: IrcMessageComponents): Command? {
                if (components.parameters.isEmpty()) {
                    return null
                }

                val user = components.parameters[0]
                val topic = components.parameters.getOrNull(1)

                return Command(channel = user, topic = topic)
            }
            
        }
        
        object Serialiser : MessageSerialiser<Command>(command) {
            
            override fun serialiseToComponents(message: Command): IrcMessageComponents {
                if (message.topic != null) {
                    return IrcMessageComponents(parameters = listOf(message.channel, message.topic))
                } else {
                    return IrcMessageComponents(parameters = listOf(message.channel))
                }
            }
            
        }
        
    }
    
    data class Message(val source: Prefix, val channel: String, val topic: String? = null) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.isEmpty() || components.prefix == null) {
                    return null
                }

                val source = PrefixParser.parse(components.prefix) ?: return null
                val user = components.parameters[0]
                val topic = components.parameters.getOrNull(1)

                return Message(source = source, channel = user, topic = topic)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val prefix = PrefixSerialiser.serialise(message.source)

                if (message.topic != null) {
                    return IrcMessageComponents(prefix = prefix, parameters = listOf(message.channel, message.topic))
                } else {
                    return IrcMessageComponents(prefix = prefix, parameters = listOf(message.channel))
                }
            }

        }
        
    }

}