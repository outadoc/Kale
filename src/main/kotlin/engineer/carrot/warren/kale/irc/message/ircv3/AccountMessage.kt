package engineer.carrot.warren.kale.irc.message.ircv3

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class AccountMessage(val source: Prefix, val account: String): IMessage {
    override val command: String = "ACCOUNT"

    companion object Factory: IMessageParser<AccountMessage>, IMessageSerialiser<AccountMessage> {

        override fun serialise(message: AccountMessage): IrcMessage? {
            val prefix = PrefixSerialiser.serialise(message.source)
            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.account))
        }

        override fun parse(message: IrcMessage): AccountMessage? {
            val prefix = message.prefix ?: return null

            if (message.parameters.size < 1) {
                return null
            }

            val source = PrefixParser.parse(prefix) ?: return null
            val account = message.parameters[0]

            return AccountMessage(source = source, account = account)
        }
    }

}