package engineer.carrot.warren.pellet.irc.message.rfc1459

import engineer.carrot.warren.pellet.irc.message.IMessage
import engineer.carrot.warren.pellet.irc.message.IMessageFactory
import engineer.carrot.warren.pellet.irc.message.IrcMessage

data class QuitMessage(val message: String? = null): IMessage {

    companion object Factory: IMessageFactory<QuitMessage> {
        override val messageType = QuitMessage::class.java
        override val command = "QUIT"

        override fun serialise(message: QuitMessage): IrcMessage? {
            if (message.message == null) {
                return IrcMessage(command = command)
            } else {
                return IrcMessage(command = command, parameters = listOf(message.message))
            }
        }

        override fun parse(message: IrcMessage): QuitMessage? {
            if (message.parameters.size < 1) {
                return QuitMessage()
            } else {
                val quitMessage = message.parameters[0]
                return QuitMessage(message = quitMessage)
            }
        }
    }

}