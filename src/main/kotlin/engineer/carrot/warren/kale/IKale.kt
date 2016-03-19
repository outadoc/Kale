package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.message.IMessage

interface IKale {
    fun <T: IMessage> register(handler: IKaleHandler<T>)

    fun process(line: String)
}