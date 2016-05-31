package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl005Message(val source: String, val target: String, val tokens: Map<String, String?>): IMessage {
    override val command: String = "005"

    companion object Factory: IMessageParser<Rpl005Message>, IMessageSerialiser<Rpl005Message> {

        override fun serialise(message: Rpl005Message): IrcMessage? {
            val tokens = mutableListOf<String>()

            for (token in message.tokens) {
                if (token.value.isNullOrEmpty()) {
                    tokens.add(token.key)
                } else {
                    tokens.add("${token.key}${CharacterCodes.EQUALS}${token.value}")
                }
            }

            val parameters = listOf(message.target) + tokens

            return IrcMessage(command = message.command, prefix = message.source, parameters = parameters)
        }

        override fun parse(message: IrcMessage): Rpl005Message? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]

            val tokens = mutableMapOf<String, String?>()

            for (i in 1..message.parameters.size - 1) {
                val token = message.parameters[i].split(delimiters = CharacterCodes.EQUALS, limit = 2)

                if (token.size <= 0 || token[0].isNullOrEmpty()) {
                    continue
                }

                var value = token.getOrNull(1)
                if (value != null && value.isEmpty()) {
                    value = null
                }

                tokens[token[0]] = value
            }

            return Rpl005Message(source = source, target = target, tokens = tokens)
        }
    }
}