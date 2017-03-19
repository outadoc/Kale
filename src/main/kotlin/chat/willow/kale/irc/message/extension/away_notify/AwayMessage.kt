package chat.willow.kale.irc.message.extension.away_notify

import chat.willow.kale.ICommand
import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object AwayMessage : ICommand {

    override val command = "AWAY"

    data class Message(val source: Prefix, val message: String?) {

        object Parser : MessageParser<Message>(command) {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                val prefix = components.prefix ?: return null

                val source = PrefixParser.parse(prefix) ?: return null
                val awayMessage = components.parameters.getOrNull(0)

                return Message(source, awayMessage)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val prefix = PrefixSerialiser.serialise(message.source)
                val awayMessage = message.message

                return if (awayMessage == null) {
                    IrcMessageComponents(prefix = prefix, parameters = listOf())
                } else {
                    IrcMessageComponents(prefix = prefix, parameters = listOf(awayMessage))
                }
            }

        }

    }

}