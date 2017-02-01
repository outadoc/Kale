package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

data class RplMonOnlineMessage(val prefix: Prefix, val nickOrStar: String, val targets: List<Prefix>): IMessage {
    override val command: String = "730"

    companion object Factory: IMessageParser<RplMonOnlineMessage>, IMessageSerialiser<RplMonOnlineMessage> {

        override fun serialise(message: RplMonOnlineMessage): IrcMessage? {
            val targets = message.targets
                                    .map { PrefixSerialiser.serialise(it) }
                                    .joinToString(separator = CharacterCodes.COMMA.toString())

            val prefix = PrefixSerialiser.serialise(message.prefix)

            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.nickOrStar, targets))
        }

        override fun parse(message: IrcMessage): RplMonOnlineMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val rawPrefix = message.prefix ?: return null
            val prefix = PrefixParser.parse(rawPrefix) ?: return null

            val nickOrStar = message.parameters[0]
            val rawTargets = message.parameters[1]

            val targets = rawTargets
                            .split(delimiters = CharacterCodes.COMMA)
                            .map { PrefixParser.parse(it) }
                            .filterNotNull()

            return RplMonOnlineMessage(prefix = prefix, nickOrStar = nickOrStar, targets = targets)
        }
    }

}
