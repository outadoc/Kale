package engineer.carrot.warren.pellet.irc.message

interface IIrcMessageSerialiser {
    fun serialise(message: IrcMessage): String?
}

