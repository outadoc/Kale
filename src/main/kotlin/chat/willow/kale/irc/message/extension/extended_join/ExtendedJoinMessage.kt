package chat.willow.kale.irc.message.extension.extended_join

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

data class ExtendedJoinMessage(val source: Prefix, val channel: String, val account: String?, val realName: String): IMessage {
    override val command: String = "JOIN"

    companion object Factory: IMessageParser<ExtendedJoinMessage>, IMessageSerialiser<ExtendedJoinMessage> {

        override fun serialise(message: ExtendedJoinMessage): IrcMessage? {
            val prefix = PrefixSerialiser.serialise(message.source)

            val account = message.account ?: "*"

            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.channel, account, message.realName))
        }

        override fun parse(message: IrcMessage): ExtendedJoinMessage? {
            if (message.parameters.size < 3 || message.prefix == null) {
                return null
            }

            val source = PrefixParser.parse(message.prefix) ?: return null
            val channel = message.parameters[0]
            val account = message.parameters[1]
            val realName = message.parameters[2]

            val parsedAccount = if (account == "*") { null } else { account }

            return ExtendedJoinMessage(source = source, channel = channel, account = parsedAccount, realName = realName)
        }
    }

}