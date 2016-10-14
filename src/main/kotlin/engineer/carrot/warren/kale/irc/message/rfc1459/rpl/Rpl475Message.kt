package engineer.carrot.warren.kale.irc.message.rfc1459.rpl

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl475Message(val source: String, val target: String, val channel: String, val contents: String): IMessage {
    override val command: String = "475"

    companion object Factory: IMessageParser<Rpl475Message>, IMessageSerialiser<Rpl475Message> {

        override fun serialise(message: Rpl475Message): IrcMessage? {
            return IrcMessage(command = message.command, prefix = message.source, parameters = listOf(message.target, message.channel, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl475Message? {
            if (message.parameters.size < 3) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val channel = message.parameters[1]
            val contents = message.parameters[2]

            return Rpl475Message(source = source, target = target, channel = channel, contents = contents)
        }
    }
}