package chat.willow.kale.irc.message.extension.sasl

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class AuthenticateMessage(val payload: String, val isEmpty: Boolean): IMessage {
    override val command: String = "AUTHENTICATE"

    companion object Factory: IMessageParser<AuthenticateMessage>, IMessageSerialiser<AuthenticateMessage> {

        override fun serialise(message: AuthenticateMessage): IrcMessage? {
            if (message.isEmpty) {
                return IrcMessage(command = "AUTHENTICATE", parameters = listOf("${CharacterCodes.PLUS}"))
            } else {
                return IrcMessage(command = "AUTHENTICATE", parameters = listOf(message.payload))
            }
        }

        override fun parse(message: IrcMessage): AuthenticateMessage? {
            if (message.parameters.isEmpty()) {
                return null
            }

            val payload = message.parameters[0]
            val isEmpty = payload == "${CharacterCodes.PLUS}"

            return AuthenticateMessage(payload = payload, isEmpty = isEmpty)
        }
    }

}