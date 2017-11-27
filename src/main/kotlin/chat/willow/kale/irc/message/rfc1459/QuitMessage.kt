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

object QuitMessage : ICommand {

    override val command = "QUIT"

    data class Command(val message: String? = null) {

        object Descriptor : KaleDescriptor<Command>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Command>() {

            override fun parseFromComponents(components: IrcMessageComponents): Command? {
                val quitMessage = components.parameters.getOrNull(0)

                return Command(quitMessage)
            }

        }

        object Serialiser : MessageSerialiser<Command>(command) {

            override fun serialiseToComponents(message: Command): IrcMessageComponents {
                if (message.message == null) {
                    return IrcMessageComponents()
                } else {
                    return IrcMessageComponents(parameters = listOf(message.message))
                }
            }
            
        }

    }

    data class Message(val source: Prefix, val message: String? = null) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.prefix == null) {
                    return null
                }

                val source = PrefixParser.parse(components.prefix) ?: return null

                val quitMessage = components.parameters.getOrNull(0)

                return Message(source = source, message = quitMessage)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val prefix = PrefixSerialiser.serialise(message.source)

                if (message.message == null) {
                    return IrcMessageComponents(prefix = prefix)
                } else {
                    return IrcMessageComponents(prefix = prefix, parameters = listOf(message.message))
                }
            }

        }
        
    }

}