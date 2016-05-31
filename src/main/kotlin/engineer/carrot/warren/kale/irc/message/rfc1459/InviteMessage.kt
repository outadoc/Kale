package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class InviteMessage(val source: Prefix? = null, val user: String, val channel: String): IMessage {
    override val command: String = "INVITE"

    companion object Factory: IMessageParser<InviteMessage>, IMessageSerialiser<InviteMessage> {

        override fun serialise(message: InviteMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }
            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.user, message.channel))
        }

        override fun parse(message: IrcMessage): InviteMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val user = message.parameters[0]
            val channel = message.parameters[1]

            return InviteMessage(source = source, user = user, channel = channel)
        }
    }

}