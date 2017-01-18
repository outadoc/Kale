package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class Rpl003Message(val source: String, val target: String, val contents: String): IMessage {
    override val command: String = "003"

    companion object Factory: IMessageParser<Rpl003Message>, IMessageSerialiser<Rpl003Message> {

        override fun serialise(message: Rpl003Message): IrcMessage? {
            return IrcMessage(command = message.command, prefix = message.source, parameters = listOf(message.target, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl003Message? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val contents = message.parameters[1]

            return Rpl003Message(source = source, target = target, contents = contents)
        }
    }
}