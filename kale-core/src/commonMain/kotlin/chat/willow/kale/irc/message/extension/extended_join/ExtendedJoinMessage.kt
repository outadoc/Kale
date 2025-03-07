package chat.willow.kale.irc.message.extension.extended_join

import chat.willow.kale.core.ICommand
import chat.willow.kale.core.message.*
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

object ExtendedJoinMessage : ICommand {

    override val command = "JOIN"

    data class Message(val source: Prefix, val channel: String, val account: String?, val realName: String) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.size < 3 || components.prefix == null) {
                    return null
                }

                val source = PrefixParser.parse(components.prefix) ?: return null
                val channel = components.parameters[0]
                val account = components.parameters[1]
                val realName = components.parameters[2]

                val parsedAccount = if (account == "*") { null } else { account }

                return Message(source, channel, parsedAccount, realName)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val prefix = PrefixSerialiser.serialise(message.source)

                val account = message.account ?: "*"

                return IrcMessageComponents(prefix = prefix, parameters = listOf(message.channel, account, message.realName))
            }

        }

    }

}