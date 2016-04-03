package engineer.carrot.warren.kale.irc.message.rfc1459

import com.google.common.base.Joiner
import com.google.common.base.Splitter
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class KickMessage(val source: String? = null, val users: List<String>, val channels: List<String>, val comment: String? = null): IMessage {

    companion object Factory: IMessageFactory<KickMessage> {
        override val messageType = KickMessage::class.java
        override val command = "KICK"

        override fun serialise(message: KickMessage): IrcMessage? {
            val channels = Joiner.on(",").join(message.channels)
            val users = Joiner.on(",").join(message.users)
            val comment = message.comment

            if (comment != null) {
                return IrcMessage(prefix = message.source, command = command, parameters = listOf(channels, users, comment))
            } else {
                return IrcMessage(prefix = message.source, command = command, parameters = listOf(channels, users))
            }
        }

        override fun parse(message: IrcMessage): KickMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix
            val channels = Splitter.on(",").split(message.parameters[0]).toList()
            val users = Splitter.on(",").split(message.parameters[1]).toList()

            if (channels.isEmpty() || channels.size != users.size) {
                return null
            }

            val comment = message.parameters.getOrNull(2)

            return KickMessage(source = source, users = users, channels = channels, comment = comment)
        }
    }

}