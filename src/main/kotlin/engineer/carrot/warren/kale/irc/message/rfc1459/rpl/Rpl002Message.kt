package engineer.carrot.warren.kale.irc.message.rfc1459.rpl

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl002Message(val source: String, val target: String, val contents: String): IMessage {
    override val command: String = "002"

    companion object Factory: IMessageParser<Rpl002Message>, IMessageSerialiser<Rpl002Message> {

        override fun serialise(message: Rpl002Message): IrcMessage? {
            return IrcMessage(command = message.command, prefix = message.source, parameters = listOf(message.target, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl002Message? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val contents = message.parameters[1]

            return Rpl002Message(source = source, target = target, contents = contents)
        }
    }
}