package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser

data class NickMessage(val source: Prefix? = null, val nickname: String, val hopcount: Int? = null): IMessage {

    companion object Factory: IMessageFactory<NickMessage> {
        override val messageType = NickMessage::class.java
        override val command = "NICK"

        override fun serialise(message: NickMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }

            var parameters = listOf(message.nickname)

            if (message.hopcount != null) {
                parameters += message.hopcount.toString()
            }

            return IrcMessage(command = command, prefix = prefix, parameters = parameters)
        }

        override fun parse(message: IrcMessage): NickMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val nickname = message.parameters[0]

            if (message.parameters.size < 2) {
                return NickMessage(source = source, nickname = nickname)
            } else {
                val hopcount = try {
                    message.parameters[1].toInt()
                } catch (exception: NumberFormatException) {
                    return null
                }

                return NickMessage(source = source, nickname = nickname, hopcount = hopcount)
            }
        }
    }

}