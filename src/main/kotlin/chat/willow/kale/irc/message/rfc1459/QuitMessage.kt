package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

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