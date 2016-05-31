package engineer.carrot.warren.kale.irc.message

interface IMessageSerialiser<M> {
    fun serialise(message: M): IrcMessage?
}

