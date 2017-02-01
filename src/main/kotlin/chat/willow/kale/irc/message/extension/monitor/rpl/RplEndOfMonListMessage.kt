package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

data class RplEndOfMonListMessage(val prefix: Prefix, val nick: String, val message: String): IMessage {
    override val command: String = "733"

    companion object Factory: IMessageParser<RplEndOfMonListMessage>, IMessageSerialiser<RplEndOfMonListMessage> {

        override fun serialise(message: RplEndOfMonListMessage): IrcMessage? {
            val prefix = PrefixSerialiser.serialise(message.prefix)

            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.nick, message.message))
        }

        override fun parse(message: IrcMessage): RplEndOfMonListMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val rawPrefix = message.prefix ?: return null
            val prefix = PrefixParser.parse(rawPrefix) ?: return null

            val nick = message.parameters[0]
            val endMessage = message.parameters[1]

            return RplEndOfMonListMessage(prefix = prefix, nick = nick, message = endMessage)
        }
    }

}