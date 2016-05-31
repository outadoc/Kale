package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class NoticeMessage(val source: Prefix? = null, val target: String, val message: String): IMessage {
    override val command: String = "NOTICE"

    companion object Factory: IMessageParser<NoticeMessage>, IMessageSerialiser<NoticeMessage> {

        override fun serialise(message: NoticeMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }
            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.target, message.message))
        }

        override fun parse(message: IrcMessage): NoticeMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val target = message.parameters[0]
            val privMessage = message.parameters[1]

            return NoticeMessage(source = source, target = target, message = privMessage)
        }
    }

}