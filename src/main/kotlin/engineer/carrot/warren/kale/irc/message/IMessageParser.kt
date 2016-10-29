package engineer.carrot.warren.kale.irc.message

interface IMessageParser<out M: IMessage> {
    fun parse(message: IrcMessage): M?
}

