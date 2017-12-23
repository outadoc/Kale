package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.core.message.ICommand
import chat.willow.kale.core.message.IrcMessageComponents
import chat.willow.kale.core.message.MessageParser
import chat.willow.kale.core.message.MessageSerialiser
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object RplEndOfMonList : ICommand {

    override val command = "733"

    data class Message(val prefix: Prefix, val nick: String, val message: String) {

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.size < 2) {
                    return null
                }

                val rawPrefix = components.prefix ?: return null
                val prefix = PrefixParser.parse(rawPrefix) ?: return null

                val nick = components.parameters[0]
                val endMessage = components.parameters[1]

                return Message(prefix, nick, endMessage)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val prefix = PrefixSerialiser.serialise(message.prefix)

                return IrcMessageComponents(prefix = prefix, parameters = listOf(message.nick, message.message))
            }

        }

    }

}