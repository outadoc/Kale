package engineer.carrot.warren.kale.irc

import engineer.carrot.warren.kale.irc.message.IrcMessage

interface IMessageSource {
    fun nextMessage(): Pair<String, IrcMessage?>?
}