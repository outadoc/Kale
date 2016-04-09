package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class UserMessage(val username: String, val mode: String, val realname: String): IMessage {

    companion object Factory: IMessageFactory<UserMessage> {
        override val messageType = UserMessage::class.java
        override val command = "USER"

        override fun serialise(message: UserMessage): IrcMessage? {
            return IrcMessage(command = UserMessage.command, parameters = listOf(message.username, message.mode, "*", message.realname))
        }

        override fun parse(message: IrcMessage): UserMessage? {
            if (message.parameters.size < 4) {
                return null
            }

            val username = message.parameters[0]
            val mode = message.parameters[1]
            val unused = message.parameters[2]
            val realname = message.parameters[3]

            return UserMessage(username = username, mode = mode, realname = realname)
        }
    }

}