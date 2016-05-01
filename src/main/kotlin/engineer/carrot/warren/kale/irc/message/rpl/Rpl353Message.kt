package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl353Message(val source: String, val target: String, val visibility: String, val channel: String, val names: List<String>): IMessage {

    companion object Factory: IMessageFactory<Rpl353Message> {
        override val messageType = Rpl353Message::class.java
        override val key = "353"

        override fun serialise(message: Rpl353Message): IrcMessage? {
            val names = message.names.joinToString(separator = CharacterCodes.SPACE.toString())

            return IrcMessage(command = key, prefix = message.source, parameters = listOf(message.target, message.visibility, message.channel, names))
        }

        override fun parse(message: IrcMessage): Rpl353Message? {
            if (message.parameters.size < 4) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val visibility = message.parameters[1]
            val channel = message.parameters[2]
            val names = message.parameters[3].split(delimiters = CharacterCodes.SPACE).filterNot { it.isEmpty() }

            return Rpl353Message(source = source, target = target, visibility = visibility, channel = channel, names = names)
        }
    }
}