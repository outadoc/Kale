package chat.willow.kale.irc.message.extension.chghost

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser

data class ChgHostMessage(val source: Prefix, val newUser: String, val newHost: String): IMessage {

    override val command: String = "CHGHOST"

    companion object Factory: IMessageParser<ChgHostMessage>, IMessageSerialiser<ChgHostMessage> {

        override fun serialise(message: ChgHostMessage): IrcMessage? {
            val prefix = PrefixSerialiser.serialise(message.source)

            return IrcMessage(command = message.command, prefix = prefix, parameters = listOf(message.newUser, message.newHost))
        }

        override fun parse(message: IrcMessage): ChgHostMessage? {
            val prefix = message.prefix ?: return null

            if (message.parameters.size < 2) {
                return null
            }

            val source = PrefixParser.parse(prefix) ?: return null
            val newUser = message.parameters[0]
            val newHost = message.parameters[1]

            return ChgHostMessage(source = source, newUser = newUser, newHost = newHost)
        }
    }

}