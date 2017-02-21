package chat.willow.kale.irc.message

interface IMessageSerialiser<in M: Any> {
    fun serialise(message: M): IrcMessage?
}

