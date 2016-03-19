package engineer.carrot.warren.kale.irc.message

interface IIrcMessageSerialiser {
    fun serialise(message: IrcMessage): String?
}

