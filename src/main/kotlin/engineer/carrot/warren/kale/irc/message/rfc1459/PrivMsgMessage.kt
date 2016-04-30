package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class PrivMsgMessage(val source: Prefix? = null, val target: String, val message: String): IMessage {

    companion object Factory: IMessageFactory<PrivMsgMessage> {
        override val messageType = PrivMsgMessage::class.java
        override val key = "PRIVMSG"

        override fun serialise(message: PrivMsgMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }
            return IrcMessage(command = key, prefix = prefix, parameters = listOf(message.target, message.message))
        }

        override fun parse(message: IrcMessage): PrivMsgMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val target = message.parameters[0]
            val privMessage = message.parameters[1]

            return PrivMsgMessage(source = source, target = target, message = privMessage)
        }
    }

}