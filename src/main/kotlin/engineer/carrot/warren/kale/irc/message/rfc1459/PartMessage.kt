package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class PartMessage(val source: Prefix? = null, val channels: List<String>): IMessage {
    override val command: String = "PART"

    companion object Factory: IMessageParser<PartMessage>, IMessageSerialiser<PartMessage> {

        override fun serialise(message: PartMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }
            val channels = message.channels.joinToString(separator = CharacterCodes.COMMA.toString())

            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(channels))
        }

        override fun parse(message: IrcMessage): PartMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val unsplitChannels = message.parameters[0]
            val channels = unsplitChannels.split(delimiters = CharacterCodes.COMMA)

            return PartMessage(source = source, channels = channels)
        }
    }

}