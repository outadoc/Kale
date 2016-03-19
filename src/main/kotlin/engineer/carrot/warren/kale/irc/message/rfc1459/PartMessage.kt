package engineer.carrot.warren.kale.irc.message.rfc1459

import com.google.common.base.Joiner
import com.google.common.base.Splitter
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class PartMessage(val channels: List<String>): IMessage {

    companion object Factory: IMessageFactory<PartMessage> {
        override val messageType = PartMessage::class.java
        override val command = "PART"

        override fun serialise(message: PartMessage): IrcMessage? {
            val channels = Joiner.on(",").join(message.channels)

            return IrcMessage(command = command, parameters = listOf(channels))
        }

        override fun parse(message: IrcMessage): PartMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            val unsplitChannels = message.parameters[0]
            val channels = Splitter.on(",").split(unsplitChannels).toList()

            return PartMessage(channels = channels)
        }
    }

}