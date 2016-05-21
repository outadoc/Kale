package engineer.carrot.warren.kale.irc.message.ircv3.sasl

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl903Message(val source: String, val target: String, val contents: String): IMessage {

    companion object Factory: IMessageFactory<Rpl903Message> {
        override val messageType = Rpl903Message::class.java
        override val key = "903"

        override fun serialise(message: Rpl903Message): IrcMessage? {
            return IrcMessage(command = key, prefix = message.source, parameters = listOf(message.target, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl903Message? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val contents = message.parameters[1]

            return Rpl903Message(source = source, target = target, contents = contents)
        }
    }
}