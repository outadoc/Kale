package engineer.carrot.warren.kale.irc.message.ircv3.sasl

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.message.ircv3.sasl.Rpl905Message
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class Rpl905Message(val source: String, val target: String, val contents: String): IMessage {

    companion object Factory: IMessageFactory<Rpl905Message> {
        override val messageType = Rpl905Message::class.java
        override val key = "905"

        override fun serialise(message: Rpl905Message): IrcMessage? {
            return IrcMessage(command = key, prefix = message.source, parameters = listOf(message.target, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl905Message? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val contents = message.parameters[1]

            return Rpl905Message(source = source, target = target, contents = contents)
        }
    }
}