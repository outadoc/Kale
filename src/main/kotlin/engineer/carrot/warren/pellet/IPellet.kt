package engineer.carrot.warren.pellet

import engineer.carrot.warren.pellet.irc.message.IMessage

interface IPellet {
    fun <T: IMessage> register(handler: IPelletHandler<T>)

    fun process(line: String)
}