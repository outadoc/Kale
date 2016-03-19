package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class UserMessage(val username: String, val hostname: String, val servername: String, val realname: String): IMessage {

    companion object Factory: IMessageFactory<UserMessage> {
        override val messageType = UserMessage::class.java
        override val command = "USER"

        override fun serialise(message: UserMessage): IrcMessage? {
            return IrcMessage(command = UserMessage.command, parameters = listOf(message.username, message.hostname, message.servername, message.realname))
        }

        override fun parse(message: IrcMessage): UserMessage? {
            if (message.parameters.size < 4) {
                return null
            }

            val username = message.parameters[0]
            val hostname = message.parameters[1]
            val servername = message.parameters[2]
            val realname = message.parameters[3]

            return UserMessage(username = username, hostname = hostname, servername = servername, realname = realname)
        }
    }

}