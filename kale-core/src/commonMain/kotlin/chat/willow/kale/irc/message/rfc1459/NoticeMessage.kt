package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.core.ICommand
import chat.willow.kale.core.message.*
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object NoticeMessage : ICommand {

    override val command = "NOTICE"

    data class Command(val target: String, val message: String) {

        object Descriptor : KaleDescriptor<Command>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Command>() {

            override fun parseFromComponents(components: IrcMessageComponents): Command? {
                if (components.parameters.size < 2) {
                    return null
                }

                val target = components.parameters[0]
                val privMessage = components.parameters[1]

                return Command(target, privMessage)
            }
            
        }
        
        object Serialiser : MessageSerialiser<Command>(command) {
            
            override fun serialiseToComponents(message: Command): IrcMessageComponents {
                return IrcMessageComponents(parameters = listOf(message.target, message.message))
            }
            
        }
        
    }

    data class Message(val source: Prefix, val target: String, val message: String) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.size < 2 || components.prefix == null) {
                    return null
                }

                val source = PrefixParser.parse(components.prefix ?: "") ?: return null
                val target = components.parameters[0]
                val privMessage = components.parameters[1]

                return Message(source = source, target = target, message = privMessage)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val prefix = PrefixSerialiser.serialise(message.source)

                return IrcMessageComponents(prefix = prefix, parameters = listOf(message.target, message.message))
            }

        }
        
    }

}