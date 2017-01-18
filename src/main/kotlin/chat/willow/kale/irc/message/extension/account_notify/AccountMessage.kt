package chat.willow.kale.irc.message.extension.account_notify

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

data class AccountMessage(val source: Prefix, val account: String): IMessage {

    override val command: String = "ACCOUNT"

    companion object Factory: IMessageParser<AccountMessage>, IMessageSerialiser<AccountMessage> {

        override fun serialise(message: AccountMessage): IrcMessage? {
            val prefix = PrefixSerialiser.serialise(message.source)
            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.account))
        }

        override fun parse(message: IrcMessage): AccountMessage? {
            val prefix = message.prefix ?: return null

            if (message.parameters.isEmpty()) {
                return null
            }

            val source = PrefixParser.parse(prefix) ?: return null
            val account = message.parameters[0]

            return AccountMessage(source = source, account = account)
        }
    }

}