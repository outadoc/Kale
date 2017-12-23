package chat.willow.kale.irc.message.extension.chghost

import chat.willow.kale.core.message.*
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object ChgHostMessage : ICommand {

    override val command = "CHGHOST"

    data class Message(val source: Prefix, val newUser: String, val newHost: String) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                val prefix = components.prefix ?: return null

                if (components.parameters.size < 2) {
                    return null
                }

                val source = PrefixParser.parse(prefix) ?: return null
                val newUser = components.parameters[0]
                val newHost = components.parameters[1]

                return Message(source, newUser, newHost)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val prefix = PrefixSerialiser.serialise(message.source)

                return IrcMessageComponents(prefix = prefix, parameters = listOf(message.newUser, message.newHost))
            }

        }

    }

}