package chat.willow.kale.irc.message.extension.sasl

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class Rpl903Message(val source: String, val target: String, val contents: String): IMessage {
    override val command: String = "903"

    companion object Factory: IMessageParser<Rpl903Message>, IMessageSerialiser<Rpl903Message> {
        override fun serialise(message: Rpl903Message): IrcMessage? {
            return IrcMessage(command = message.command, prefix = message.source, parameters = listOf(message.target, message.contents))
        }

        override fun parse(message: IrcMessage): Rpl903Message? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val contents = message.parameters[1]

            return Rpl903Message(source = source, target = target, contents = contents)
        }
    }
}