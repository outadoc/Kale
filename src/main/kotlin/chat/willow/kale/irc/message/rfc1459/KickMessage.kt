package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

data class KickMessage(val source: Prefix? = null, val users: List<String>, val channels: List<String>, val comment: String? = null): IMessage {
    override val command: String = "KICK"

    companion object Factory: IMessageParser<KickMessage>, IMessageSerialiser<KickMessage> {

        override fun serialise(message: KickMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }
            val channels = message.channels.joinToString(separator = CharacterCodes.COMMA.toString())
            val users = message.users.joinToString(separator = CharacterCodes.COMMA.toString())
            val comment = message.comment

            if (comment != null) {
                return IrcMessage(prefix = prefix, command = message.command, parameters = listOf(channels, users, comment))
            } else {
                return IrcMessage(prefix = prefix, command = message.command, parameters = listOf(channels, users))
            }
        }

        override fun parse(message: IrcMessage): KickMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val channels = message.parameters[0].split(delimiters = CharacterCodes.COMMA)
            val users = message.parameters[1].split(delimiters = CharacterCodes.COMMA)

            if (channels.isEmpty() || channels.size != users.size) {
                return null
            }

            val comment = message.parameters.getOrNull(2)

            return KickMessage(source = source, users = users, channels = channels, comment = comment)
        }
    }

}