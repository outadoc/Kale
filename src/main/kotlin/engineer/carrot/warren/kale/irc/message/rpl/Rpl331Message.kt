package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl331Message(val source: String, val channel: String, val contents: String): IMessage {

    companion object Factory: IMessageFactory<Rpl331Message> {
        override val messageType = Rpl331Message::class.java
        override val command = "331"

        override fun serialise(message: Rpl331Message): IrcMessage? {
            return IrcMessage(command = command, prefix = message.source, parameters = listOf(message.channel, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl331Message? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix ?: ""
            val channel = message.parameters[0]
            val contents = message.parameters[1]

            return Rpl331Message(source = source, channel = channel, contents = contents)
        }
    }
}