package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class Rpl353Message(val source: String, val target: String, val visibility: String, val channel: String, val names: List<String>): IMessage {
    override val command: String = "353"

    companion object Factory: IMessageParser<Rpl353Message>, IMessageSerialiser<Rpl353Message> {

        override fun serialise(message: Rpl353Message): IrcMessage? {
            val names = message.names.joinToString(separator = CharacterCodes.SPACE.toString())

            return IrcMessage(command = message.command, prefix = message.source, parameters = listOf(message.target, message.visibility, message.channel, names))
        }

        override fun parse(message: IrcMessage): Rpl353Message? {
            if (message.parameters.size < 4) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]
            val visibility = message.parameters[1]
            val channel = message.parameters[2]
            val names = message.parameters[3].split(delimiters = CharacterCodes.SPACE).filterNot(String::isEmpty)

            return Rpl353Message(source = source, target = target, visibility = visibility, channel = channel, names = names)
        }
    }
}