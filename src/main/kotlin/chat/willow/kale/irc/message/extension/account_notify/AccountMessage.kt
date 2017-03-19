package chat.willow.kale.irc.message.extension.account_notify

import chat.willow.kale.ICommand
import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object AccountMessage : ICommand {

    override val command = "ACCOUNT"

    data class Message(val source: Prefix, val account: String) {

        object Parser : MessageParser<Message>(command) {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                val prefix = components.prefix ?: return null

                if (components.parameters.isEmpty()) {
                    return null
                }

                val source = PrefixParser.parse(prefix) ?: return null
                val account = components.parameters[0]

                return Message(source, account)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val prefix = PrefixSerialiser.serialise(message.source)

                val parameters = listOf(message.account)

                return IrcMessageComponents(prefix = prefix, parameters = parameters)
            }

        }

    }

}