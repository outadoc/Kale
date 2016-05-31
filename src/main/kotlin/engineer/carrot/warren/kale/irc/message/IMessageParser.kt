package engineer.carrot.warren.kale.irc.message

interface IMessageParser<M: IMessage> {
    fun parse(message: IrcMessage): M?
}

