package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class Rpl471Message(val source: String, val target: String, val channel: String, val contents: String): IMessage {
    override val command: String = "471"

    companion object Factory: IMessageParser<Rpl471Message>, IMessageSerialiser<Rpl471Message> {

        override fun serialise(message: Rpl471Message): IrcMessage? {
            return IrcMessage(command = message.command, prefix = message.source, parameters = listOf(message.target, message.channel, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl471Message? {
            if (message.parameters.size < 3) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val channel = message.parameters[1]
            val contents = message.parameters[2]

            return Rpl471Message(source = source, target = target, channel = channel, contents = contents)
        }
    }
}