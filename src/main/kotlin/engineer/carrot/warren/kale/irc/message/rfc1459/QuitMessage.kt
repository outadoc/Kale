package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class QuitMessage(val source: Prefix? = null, val message: String? = null): IMessage {

    companion object Factory: IMessageFactory<QuitMessage> {
        override val messageType = QuitMessage::class.java
        override val key = "QUIT"

        override fun serialise(message: QuitMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }

            if (message.message == null) {
                return IrcMessage(command = key, prefix = prefix)
            } else {
                return IrcMessage(command = key, prefix = prefix, parameters = listOf(message.message))
            }
        }

        override fun parse(message: IrcMessage): QuitMessage? {
            val source = PrefixParser.parse(message.prefix ?: "")

            val quitMessage = message.parameters.getOrNull(0)

            return QuitMessage(source = source, message = quitMessage)
        }
    }

}