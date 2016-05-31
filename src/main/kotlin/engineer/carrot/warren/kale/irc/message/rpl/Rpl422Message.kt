package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl422Message(val source: String, val target: String, val contents: String): IMessage {
    override val command: String = "422"

    companion object Factory: IMessageParser<Rpl422Message>, IMessageSerialiser<Rpl422Message> {

        override fun serialise(message: Rpl422Message): IrcMessage? {
            return IrcMessage(command = message.command, prefix = message.source, parameters = listOf(message.target, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl422Message? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val contents = message.parameters[1]

            return Rpl422Message(source = source, target = target, contents = contents)
        }
    }
}