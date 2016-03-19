package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.message.IMessage

interface IKaleHandler<T: IMessage> {
    val messageType: Class<T>

    fun handle(messageOne: T)
}