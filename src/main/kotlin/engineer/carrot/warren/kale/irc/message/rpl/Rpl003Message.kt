package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl003Message(val source: String, val target: String, val contents: String): IMessage {

    companion object Factory: IMessageFactory<Rpl003Message> {
        override val messageType = Rpl003Message::class.java
        override val key = "003"

        override fun serialise(message: Rpl003Message): IrcMessage? {
            return IrcMessage(command = key, prefix = message.source, parameters = listOf(message.target, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl003Message? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val contents = message.parameters[1]

            return Rpl003Message(source = source, target = target, contents = contents)
        }
    }
}