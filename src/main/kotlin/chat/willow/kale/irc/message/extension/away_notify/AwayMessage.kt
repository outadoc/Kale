package chat.willow.kale.irc.message.extension.away_notify

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

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