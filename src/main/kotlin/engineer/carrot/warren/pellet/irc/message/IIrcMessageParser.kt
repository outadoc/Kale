package engineer.carrot.warren.pellet.irc.message

interface IIrcMessageParser {
    fun parse(line: String): IrcMessage?
}