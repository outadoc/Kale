package engineer.carrot.warren.kale.irc

import engineer.carrot.warren.kale.irc.message.IrcMessage

interface IMessageSink {
    fun writeMessage(message: IrcMessage)
}