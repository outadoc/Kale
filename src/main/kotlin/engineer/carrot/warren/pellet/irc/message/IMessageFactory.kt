package engineer.carrot.warren.pellet.irc.message

interface IMessageFactory<T: IMessage> {
    val messageType: Class<T>

    val command: String

    fun parse(message: IrcMessage): T?

    fun serialise(messageOne: T): IrcMessage?
}