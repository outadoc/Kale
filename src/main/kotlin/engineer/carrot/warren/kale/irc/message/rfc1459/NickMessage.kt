package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class NickMessage(val nickname: String, val hopcount: Int? = null): IMessage {

    companion object Factory: IMessageFactory<NickMessage> {
        override val messageType = NickMessage::class.java
        override val command = "NICK"

        override fun serialise(message: NickMessage): IrcMessage? {
            var parameters = listOf(message.nickname)

            if (message.hopcount != null) {
                parameters += message.hopcount.toString()
            }

            return IrcMessage(command = command, parameters = parameters)
        }

        override fun parse(message: IrcMessage): NickMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            val nickname = message.parameters[0]

            if (message.parameters.size < 2) {
                return NickMessage(nickname = nickname)
            } else {
                val hopcount = try {
                    message.parameters[1].toInt()
                } catch (exception: NumberFormatException) {
                    return null
                }

                return NickMessage(nickname = nickname, hopcount = hopcount)
            }
        }
    }

}