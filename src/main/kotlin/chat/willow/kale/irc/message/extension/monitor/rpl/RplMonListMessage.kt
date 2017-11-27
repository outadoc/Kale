package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.ICommand
import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object RplMonList : ICommand {

    override val command = "732"

    data class Message(val prefix: Prefix, val nick: String, val targets: List<Prefix>) {

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.size < 2) {
                    return null
                }

                val rawPrefix = components.prefix ?: return null
                val prefix = PrefixParser.parse(rawPrefix) ?: return null

                val nick = components.parameters[0]
                val rawTargets = components.parameters[1]

                val targets = rawTargets
                        .split(delimiters = CharacterCodes.COMMA)
                        .mapNotNull { PrefixParser.parse(it) }

                return Message(prefix = prefix, nick = nick, targets = targets)
            }
        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val targets = message.targets.joinToString(separator = CharacterCodes.COMMA.toString()) { PrefixSerialiser.serialise(it) }

                val prefix = PrefixSerialiser.serialise(message.prefix)

                return IrcMessageComponents(prefix = prefix, parameters = listOf(message.nick, targets))
            }
        }

    }

}