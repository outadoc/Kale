package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.generator.message.ICommand
import chat.willow.kale.generator.message.IrcMessageComponents
import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.generator.message.MessageParser
import chat.willow.kale.generator.message.MessageSerialiser
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object RplMonListIsFull : ICommand {

    override val command = "734"

    data class Message(val prefix: Prefix, val nick: String, val limit: String, val targets: List<Prefix>, val message: String) {

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.size < 4) {
                    return null
                }

                val rawPrefix = components.prefix ?: return null
                val prefix = PrefixParser.parse(rawPrefix) ?: return null

                val nick = components.parameters[0]
                val limit = components.parameters[1]
                val rawTargets = components.parameters[2]

                val targets = rawTargets
                        .split(delimiters = CharacterCodes.COMMA)
                        .mapNotNull { PrefixParser.parse(it) }

                val endMessage = components.parameters[3]

                return Message(prefix = prefix, nick = nick, limit = limit, targets = targets, message = endMessage)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val targets = message.targets.joinToString(separator = CharacterCodes.COMMA.toString()) { PrefixSerialiser.serialise(it) }

                val prefix = PrefixSerialiser.serialise(message.prefix)

                return IrcMessageComponents(prefix = prefix, parameters = listOf(message.nick, message.limit, targets, message.message))
            }

        }

    }

}
