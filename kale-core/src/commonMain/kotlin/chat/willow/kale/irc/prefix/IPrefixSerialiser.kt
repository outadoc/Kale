package chat.willow.kale.irc.prefix

interface IPrefixSerialiser {
    fun serialise(prefix: Prefix): String
}