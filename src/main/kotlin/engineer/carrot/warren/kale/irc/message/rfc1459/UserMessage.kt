package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class UserMessage(val username: String, val mode: String, val realname: String): IMessage {
    override val command: String = "USER"

    companion object Factory: IMessageParser<UserMessage>, IMessageSerialiser<UserMessage> {

        override fun serialise(message: UserMessage): IrcMessage? {
            return IrcMessage(command = message.command, parameters = listOf(message.username, message.mode, "*", message.realname))
        }

        override fun parse(message: IrcMessage): UserMessage? {
            if (message.parameters.size < 4) {
                return null
            }

            val username = message.parameters[0]
            val mode = message.parameters[1]
            @Suppress("UNUSED_VARIABLE") val unused = message.parameters[2]
            val realname = message.parameters[3]

            return UserMessage(username = username, mode = mode, realname = realname)
        }
    }

}