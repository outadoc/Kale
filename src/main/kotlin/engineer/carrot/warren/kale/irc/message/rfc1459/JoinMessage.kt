package engineer.carrot.warren.kale.irc.message.rfc1459

import com.google.common.base.Joiner
import com.google.common.base.Splitter
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class JoinMessage(val channels: List<String>, val keys: List<String>? = null): IMessage {

    companion object Factory: IMessageFactory<JoinMessage> {
        override val messageType = JoinMessage::class.java
        override val command = "JOIN"

        override fun serialise(message: JoinMessage): IrcMessage? {
            val channels = Joiner.on(",").join(message.channels)

            if (message.keys == null || message.keys.isEmpty()) {
                return IrcMessage(command = command, parameters = listOf(channels))
            } else {
                val keys = Joiner.on(",").join(message.keys)

                return IrcMessage(command = command, parameters = listOf(channels, keys))
            }
        }

        override fun parse(message: IrcMessage): JoinMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            val unsplitChannels = message.parameters[0]
            val channels = Splitter.on(",").split(unsplitChannels).toList()

            if (message.parameters.size < 2) {
                return JoinMessage(channels = channels)
            } else {
                val unsplitKeys = message.parameters[1]
                val keys = Splitter.on(",").split(unsplitKeys).toList()

                return JoinMessage(channels = channels, keys = keys)
            }
        }
    }

}