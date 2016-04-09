package engineer.carrot.warren.kale.irc.prefix

interface IPrefixSerialiser {
    fun serialise(prefix: Prefix): String
}