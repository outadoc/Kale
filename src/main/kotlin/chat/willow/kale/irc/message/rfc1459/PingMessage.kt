package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.ICommand
import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.KaleDescriptor
import chat.willow.kale.commandMatcher
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser

object PingMessage : ICommand {

    override val command = "PING"

    data class Command(val token: String) {

        object Descriptor : KaleDescriptor<Command>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Command>() {

            override fun parseFromComponents(components: IrcMessageComponents): Command? {
                if (components.parameters.isEmpty()) {
                    return null
                }

                val token = components.parameters[0]

                return Command(token)
            }

        }

        object Serialiser : MessageSerialiser<Command>(command) {

            override fun serialiseToComponents(message: Command): IrcMessageComponents {
                val parameters = listOf(message.token)

                return IrcMessageComponents(parameters)
            }

        }

    }

}