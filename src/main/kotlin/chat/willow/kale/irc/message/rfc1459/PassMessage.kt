package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class PassMessage(val password: String): IMessage {
    override val command: String = "PASS"

    companion object Factory: IMessageParser<PassMessage>, IMessageSerialiser<PassMessage> {

        override fun serialise(message: PassMessage): IrcMessage? {
            return IrcMessage(command = message.command, parameters = listOf(message.password))
        }

        override fun parse(message: IrcMessage): PassMessage? {
            if (message.parameters.isEmpty()) {
                return null
            }

            val password = message.parameters[0]

            return PassMessage(password = password)
        }
    }

}