package chat.willow.kale.irc.message.extension.batch

import chat.willow.kale.*
import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.PrefixSubcommandParser
import chat.willow.kale.irc.message.PrefixSubcommandSerialiser
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object BatchMessage : ICommand {

    override val command = "BATCH"

    object Start : ISubcommand {

        override val subcommand = CharacterCodes.PLUS.toString()

        data class Message(val source: Prefix, val reference: String, val type: String, val parameters: List<String> = listOf()) {

            // BATCH +something
            // todo: descriptor

            object Parser : PrefixSubcommandParser<Message>(subcommand) {

                override fun parseFromComponents(components: IrcMessageComponents): Message? {
                    if (components.parameters.size < 2) {
                        return null
                    }

                    val source = PrefixParser.parse(components.prefix ?: "") ?: return null
                    val reference = components.parameters[0]
                    val type = components.parameters[1]
                    val parameters = components.parameters.drop(2)

                    return Message(source, reference, type, parameters)
                }

            }

            object Serialiser : PrefixSubcommandSerialiser<Message>(command, subcommand) {

                override fun serialiseToComponents(message: Message): IrcMessageComponents {
                    val parameters = listOf(message.reference, message.type) + message.parameters

                    val prefix = PrefixSerialiser.serialise(message.source)

                    return IrcMessageComponents(prefix = prefix, parameters = parameters)
                }

            }

        }

    }

    object End : ISubcommand {

        override val subcommand = CharacterCodes.MINUS.toString()

        data class Message(val source: Prefix, val reference: String) {

            // BATCH -something
            // todo: descriptor

            object Parser : PrefixSubcommandParser<Message>(subcommand) {

                override fun parseFromComponents(components: IrcMessageComponents): Message? {
                    if (components.parameters.isEmpty()) {
                        return null
                    }

                    val source = PrefixParser.parse(components.prefix ?: "") ?: return null
                    val reference = components.parameters[0]

                    return Message(source, reference)
                }

            }

            object Serialiser : PrefixSubcommandSerialiser<Message>(command, subcommand) {

                override fun serialiseToComponents(message: Message): IrcMessageComponents {
                    val prefix = PrefixSerialiser.serialise(message.source)
                    val parameters = listOf(message.reference)

                    return IrcMessageComponents(prefix = prefix, parameters = parameters)
                }

            }

        }

    }

}