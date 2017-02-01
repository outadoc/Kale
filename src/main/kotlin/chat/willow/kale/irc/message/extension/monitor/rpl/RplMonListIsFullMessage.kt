package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

data class RplMonListIsFullMessage(val prefix: Prefix, val nick: String, val limit: String, val targets: List<Prefix>, val message: String): IMessage {
    override val command: String = "734"

    companion object Factory: IMessageParser<RplMonListIsFullMessage>, IMessageSerialiser<RplMonListIsFullMessage> {

        override fun serialise(message: RplMonListIsFullMessage): IrcMessage? {
            val targets = message.targets
                    .map { PrefixSerialiser.serialise(it) }
                    .joinToString(separator = CharacterCodes.COMMA.toString())

            val prefix = PrefixSerialiser.serialise(message.prefix)

            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.nick, message.limit, targets, message.message))
        }

        override fun parse(message: IrcMessage): RplMonListIsFullMessage? {
            if (message.parameters.size < 4) {
                return null
            }

            val rawPrefix = message.prefix ?: return null
            val prefix = PrefixParser.parse(rawPrefix) ?: return null

            val nick = message.parameters[0]
            val limit = message.parameters[1]
            val rawTargets = message.parameters[2]

            val targets = rawTargets
                    .split(delimiters = CharacterCodes.COMMA)
                    .map { PrefixParser.parse(it) }
                    .filterNotNull()

            val endMessage = message.parameters[3]

            return RplMonListIsFullMessage(prefix = prefix, nick = nick, limit = limit, targets = targets, message = endMessage)
        }
    }

}