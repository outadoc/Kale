package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IrcMessage

interface IKale {
    fun <T: IMessage> register(handler: IKaleHandler<T>)

    fun process(line: String)

    fun <T: IMessage> serialise(message: T): IrcMessage?
}