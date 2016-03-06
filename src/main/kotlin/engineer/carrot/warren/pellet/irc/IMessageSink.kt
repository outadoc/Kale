package engineer.carrot.warren.pellet.irc

import engineer.carrot.warren.pellet.irc.message.IrcMessage

interface IMessageSink {
    fun writeMessage(message: IrcMessage)
}