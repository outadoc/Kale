package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.core.message.*

object PassMessage : ICommand {

    override val command = "PASS"

    data class Command(val password: String) {

        object Descriptor : KaleDescriptor<Command>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Command>() {

            override fun parseFromComponents(components: IrcMessageComponents): Command? {
                if (components.parameters.isEmpty()) {
                    return null
                }

                val password = components.parameters[0]

                return Command(password)
            }

        }

        object Serialiser : MessageSerialiser<Command>(command) {

            override fun serialiseToComponents(message: Command): IrcMessageComponents {
                return IrcMessageComponents(parameters = listOf(message.password))
            }
        }

    }

}