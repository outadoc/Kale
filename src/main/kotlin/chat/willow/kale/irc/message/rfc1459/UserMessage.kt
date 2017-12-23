package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.core.message.*

object UserMessage : ICommand {

    override val command = "USER"

    data class Command(val username: String, val mode: String, val realname: String) {

        object Descriptor : KaleDescriptor<Command>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Command>() {

            override fun parseFromComponents(components: IrcMessageComponents): Command? {
                if (components.parameters.size < 4) {
                    return null
                }

                val username = components.parameters[0]
                val mode = components.parameters[1]
                @Suppress("UNUSED_VARIABLE") val unused = components.parameters[2]
                val realname = components.parameters[3]

                return Command(username, mode, realname)
            }

        }

        object Serialiser : MessageSerialiser<Command>(command) {

            override fun serialiseToComponents(message: Command): IrcMessageComponents {
                return IrcMessageComponents(parameters = listOf(message.username, message.mode, "*", message.realname))
            }

        }

    }

}