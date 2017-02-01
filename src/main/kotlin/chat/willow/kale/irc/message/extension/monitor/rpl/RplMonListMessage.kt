package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

data class RplMonListMessage(val prefix: Prefix, val nick: String, val targets: List<Prefix>): IMessage {
    override val command: String = "732"

    companion object Factory: IMessageParser<RplMonListMessage>, IMessageSerialiser<RplMonListMessage> {

        override fun serialise(message: RplMonListMessage): IrcMessage? {
            val targets = message.targets
                    .map { PrefixSerialiser.serialise(it) }
                    .joinToString(separator = CharacterCodes.COMMA.toString())

            val prefix = PrefixSerialiser.serialise(message.prefix)

            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.nick, targets))
        }

        override fun parse(message: IrcMessage): RplMonListMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val rawPrefix = message.prefix ?: return null
            val prefix = PrefixParser.parse(rawPrefix) ?: return null

            val nick = message.parameters[0]
            val rawTargets = message.parameters[1]

            val targets = rawTargets
                    .split(delimiters = CharacterCodes.COMMA)
                    .map { PrefixParser.parse(it) }
                    .filterNotNull()

            return RplMonListMessage(prefix = prefix, nick = nick, targets = targets)
        }
    }

}