package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class PrivMsgMessage(val source: Prefix? = null, val time: String? = null, val target: String, val message: String): IMessage {
    override val command: String = "PRIVMSG"

    companion object Factory: IMessageParser<PrivMsgMessage>, IMessageSerialiser<PrivMsgMessage> {

        override fun serialise(message: PrivMsgMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }
            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.target, message.message))
        }

        override fun parse(message: IrcMessage): PrivMsgMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val target = message.parameters[0]
            val privMessage = message.parameters[1]

            val serverTime = message.tags["time"]

            return PrivMsgMessage(source = source, time = serverTime, target = target, message = privMessage)
        }
    }

}