package engineer.carrot.warren.kale.irc.prefix

interface IPrefixParser {
    fun parse(rawPrefix: String): Prefix?
}