package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class QuitMessage(val source: Prefix? = null, val message: String? = null): IMessage {
    override val command: String = "QUIT"

    companion object Factory: IMessageParser<QuitMessage>, IMessageSerialiser<QuitMessage> {

        override fun serialise(message: QuitMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }

            if (message.message == null) {
                return IrcMessage(command = message.command, prefix = prefix)
            } else {
                return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.message))
            }
        }

        override fun parse(message: IrcMessage): QuitMessage? {
            val source = PrefixParser.parse(message.prefix ?: "")

            val quitMessage = message.parameters.getOrNull(0)

            return QuitMessage(source = source, message = quitMessage)
        }
    }

}