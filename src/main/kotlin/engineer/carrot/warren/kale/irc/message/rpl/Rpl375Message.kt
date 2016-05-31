package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl375Message(val source: String, val target: String, val contents: String): IMessage {
    override val command: String = "375"

    companion object Factory: IMessageParser<Rpl375Message>, IMessageSerialiser<Rpl375Message> {

        override fun serialise(message: Rpl375Message): IrcMessage? {
            return IrcMessage(command = message.command, prefix = message.source, parameters = listOf(message.target, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl375Message? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val contents = message.parameters[1]

            return Rpl375Message(source = source, target = target, contents = contents)
        }
    }
}