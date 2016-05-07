package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl473Message(val source: String, val target: String, val channel: String, val contents: String): IMessage {

    companion object Factory: IMessageFactory<Rpl473Message> {
        override val messageType = Rpl473Message::class.java
        override val key = "473"

        override fun serialise(message: Rpl473Message): IrcMessage? {
            return IrcMessage(command = key, prefix = message.source, parameters = listOf(message.target, message.channel, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl473Message? {
            if (message.parameters.size < 3) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val channel = message.parameters[1]
            val contents = message.parameters[2]

            return Rpl473Message(source = source, target = target, channel = channel, contents = contents)
        }
    }
}