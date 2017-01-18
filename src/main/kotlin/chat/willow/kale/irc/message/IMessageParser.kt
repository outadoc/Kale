package chat.willow.kale.irc.message

interface IMessageParser<out M: IMessage> {
    fun parse(message: IrcMessage): M?
}

