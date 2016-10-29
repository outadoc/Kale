package engineer.carrot.warren.kale.irc.message

interface IMessageSerialiser<in M> {
    fun serialise(message: M): IrcMessage?
}

