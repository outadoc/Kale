package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.ICommand
import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser

object UserMessage : ICommand {

    override val command = "USER"

    data class Command(val username: String, val mode: String, val realname: String) {

        object Parser : MessageParser<Command>(command) {

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