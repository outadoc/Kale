package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.ICommand
import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser

object PrivMsgMessage : ICommand {

    override val command = "PRIVMSG"

    data class Command(val target: String, val message: String) {

        object Parser : MessageParser<Command>(command) {

            override fun parseFromComponents(components: IrcMessageComponents): Command? {
                if (components.parameters.size < 2) {
                    return null
                }

                val target = components.parameters[0]
                val message = components.parameters[1]

                return Command(target, message)
            }
        }

        object Serialiser : MessageSerialiser<Command>(command) {

            override fun serialiseToComponents(message: Command): IrcMessageComponents {
                val parameters = listOf(message.target, message.message)

                return IrcMessageComponents(parameters)
            }

        }

    }

    data class Message(val source: Prefix, val target: String, val message: String) {

        object Parser : MessageParser<Message>(command) {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.size < 2) {
                    return null
                }

                val source = PrefixParser.parse(components.prefix ?: "") ?: return null
                val target = components.parameters[0]
                val message = components.parameters[1]

                return Message(source, target, message)
            }

        }

    }

}
