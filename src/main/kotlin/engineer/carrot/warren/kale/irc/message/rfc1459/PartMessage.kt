package engineer.carrot.warren.kale.irc.message.rfc1459

import com.google.common.base.Joiner
import com.google.common.base.Splitter
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class PartMessage(val source: Prefix? = null, val channels: List<String>): IMessage {

    companion object Factory: IMessageFactory<PartMessage> {
        override val messageType = PartMessage::class.java
        override val command = "PART"

        override fun serialise(message: PartMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }
            val channels = Joiner.on(",").join(message.channels)

            return IrcMessage(command = command, prefix = prefix, parameters = listOf(channels))
        }

        override fun parse(message: IrcMessage): PartMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val unsplitChannels = message.parameters[0]
            val channels = Splitter.on(",").split(unsplitChannels).toList()

            return PartMessage(source = source, channels = channels)
        }
    }

}