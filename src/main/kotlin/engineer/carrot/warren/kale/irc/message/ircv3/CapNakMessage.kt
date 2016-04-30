package engineer.carrot.warren.kale.irc.message.ircv3

import com.google.common.base.Joiner
import com.google.common.base.Splitter
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class CapNakMessage(val target: String? = null, val caps: List<String>): IMessage {

    companion object Factory: IMessageFactory<CapNakMessage> {
        override val messageType = CapNakMessage::class.java
        override val key = "CAPNAK"

        override fun serialise(message: CapNakMessage): IrcMessage? {
            val caps = Joiner.on(' ').join(message.caps)

            if (message.target != null) {
                return IrcMessage(command = "CAP", parameters = listOf(message.target, "NAK", caps))
            } else {
                return IrcMessage(command = "CAP", parameters = listOf("NAK", caps))
            }
        }

        override fun parse(message: IrcMessage): CapNakMessage? {
            if (message.parameters.size < 3) {
                return null
            }

            val target = message.parameters[0]
            val subCommand = message.parameters[1]
            val rawCaps = message.parameters[2]

            val caps = Splitter.on(' ').omitEmptyStrings().split(rawCaps).toList()

            return CapNakMessage(target = target, caps = caps)
        }
    }

}