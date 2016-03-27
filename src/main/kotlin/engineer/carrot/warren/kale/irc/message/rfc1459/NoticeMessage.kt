package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class NoticeMessage(val source: String? = null, val target: String, val message: String): IMessage {

    companion object Factory: IMessageFactory<NoticeMessage> {
        override val messageType = NoticeMessage::class.java
        override val command = "NOTICE"

        override fun serialise(message: NoticeMessage): IrcMessage? {
            return IrcMessage(command = command, prefix = message.source, parameters = listOf(message.target, message.message))
        }

        override fun parse(message: IrcMessage): NoticeMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            val target = message.parameters[0]
            val privMessage = message.parameters[1]

            return NoticeMessage(source = message.prefix, target = target, message = privMessage)
        }
    }

}