package engineer.carrot.warren.kale.irc.message.extension.away_notify

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class AwayMessage(val source: Prefix, val message: String?): IMessage {

    override val command: String = "AWAY"

    companion object Factory: IMessageParser<AwayMessage>, IMessageSerialiser<AwayMessage> {

        override fun serialise(message: AwayMessage): IrcMessage? {
            val prefix = PrefixSerialiser.serialise(message.source)
            val awayMessage = message.message

            if (awayMessage == null) {
                return IrcMessage(command = message.command, prefix = prefix, parameters = listOf())
            } else {
                return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(awayMessage))
            }
        }

        override fun parse(message: IrcMessage): AwayMessage? {
            val prefix = message.prefix ?: return null

            val source = PrefixParser.parse(prefix) ?: return null
            val awayMessage = message.parameters.getOrNull(0)

            return AwayMessage(source = source, message = awayMessage)
        }
    }

}