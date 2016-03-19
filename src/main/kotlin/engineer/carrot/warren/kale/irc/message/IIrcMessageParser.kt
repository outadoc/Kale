package engineer.carrot.warren.kale.irc.message

interface IIrcMessageParser {
    fun parse(line: String): IrcMessage?
}