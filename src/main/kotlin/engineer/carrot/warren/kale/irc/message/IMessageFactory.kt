package engineer.carrot.warren.kale.irc.message

interface IMessageFactory<T: IMessage> {
    val messageType: Class<T>

    val command: String

    fun parse(message: IrcMessage): T?

    fun serialise(message: T): IrcMessage?
}