package chat.willow.kale.irc.prefix

interface IPrefixParser {
    fun parse(rawPrefix: String): Prefix?
}