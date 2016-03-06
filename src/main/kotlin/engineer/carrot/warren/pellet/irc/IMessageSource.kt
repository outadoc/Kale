package engineer.carrot.warren.pellet.irc

import engineer.carrot.warren.pellet.irc.message.IrcMessage

interface IMessageSource {
    fun nextMessage(): Pair<String, IrcMessage?>?
}