package engineer.carrot.warren.kale.irc.message.rpl

import com.google.common.base.Splitter
import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class Rpl005Message(val source: String, val target: String, val tokens: Map<String, String?>): IMessage {

    companion object Factory: IMessageFactory<Rpl005Message> {
        override val messageType = Rpl005Message::class.java
        override val key = "005"

        override fun serialise(message: Rpl005Message): IrcMessage? {
            var tokens = mutableListOf<String>()

            for (token in message.tokens) {
                if (token.value.isNullOrEmpty()) {
                    tokens.add(token.key)
                } else {
                    tokens.add("${token.key}${CharacterCodes.EQUALS}${token.value}")
                }
            }

            val parameters = listOf(message.target) + tokens

            return IrcMessage(command = key, prefix = message.source, parameters = parameters)
        }

        override fun parse(message: IrcMessage): Rpl005Message? {
            if (message.parameters.size < 2) {
                return null
            }

            val source = message.prefix ?: ""
            val target = message.parameters[0]

            var tokens = mutableMapOf<String, String?>()

            for (i in 1..message.parameters.size - 1) {
                val token = Splitter.on(CharacterCodes.EQUALS).limit(2).splitToList(message.parameters[i])

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