package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class JoinMessage(val source: Prefix? = null, val channels: List<String>, val keys: List<String>? = null): IMessage {

    companion object Factory: IMessageFactory<JoinMessage> {
        override val messageType = JoinMessage::class.java
        override val key = "JOIN"

        override fun serialise(message: JoinMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }
            val channels = message.channels.joinToString(separator = CharacterCodes.COMMA.toString())

            if (message.keys == null || message.keys.isEmpty()) {
                return IrcMessage(command = key, prefix = prefix, parameters = listOf(channels))
            } else {
                val keys = message.keys.joinToString(separator = CharacterCodes.COMMA.toString())

                return IrcMessage(command = key, prefix = prefix, parameters = listOf(channels, keys))
            }
        }

        override fun parse(message: IrcMessage): JoinMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val unsplitChannels = message.parameters[0]
            val channels = unsplitChannels.split(delimiters = CharacterCodes.COMMA).filterNot { it.isEmpty() }

            if (message.parameters.size < 2) {
                return JoinMessage(source = source, channels = channels)
            } else {
                val unsplitKeys = message.parameters[1]
                val keys = unsplitKeys.split(delimiters = CharacterCodes.COMMA).filterNot { it.isEmpty() }

                return JoinMessage(source = source, channels = channels, keys = keys)
            }
        }
    }

}