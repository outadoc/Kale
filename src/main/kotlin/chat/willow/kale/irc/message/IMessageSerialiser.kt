package chat.willow.kale.irc.message

interface IMessageSerialiser<in M> {
    fun serialise(message: M): IrcMessage?
}

