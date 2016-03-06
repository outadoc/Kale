package engineer.carrot.warren.pellet

import engineer.carrot.warren.pellet.irc.message.IMessage

interface IPelletHandler<T: IMessage> {
    val messageType: Class<T>

    fun handle(messageOne: T)
}