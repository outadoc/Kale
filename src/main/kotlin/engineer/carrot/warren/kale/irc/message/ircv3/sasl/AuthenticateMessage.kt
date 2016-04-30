package engineer.carrot.warren.kale.irc.message.ircv3.sasl

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class AuthenticateMessage(val payload: String, val isEmpty: Boolean): IMessage {

    companion object Factory: IMessageFactory<AuthenticateMessage> {
        override val messageType = AuthenticateMessage::class.java
        override val key = "AUTHENTICATE"

        override fun serialise(message: AuthenticateMessage): IrcMessage? {
            if (message.isEmpty) {
                return IrcMessage(command = "AUTHENTICATE", parameters = listOf("${CharacterCodes.PLUS}"))
            } else {
                return IrcMessage(command = "AUTHENTICATE", parameters = listOf(message.payload))
            }
        }

        override fun parse(message: IrcMessage): AuthenticateMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            val payload = message.parameters[0]
            var isEmpty = payload == "${CharacterCodes.PLUS}"

            return AuthenticateMessage(payload = payload, isEmpty = isEmpty)
        }
    }

}