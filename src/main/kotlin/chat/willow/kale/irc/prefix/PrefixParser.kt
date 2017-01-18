package chat.willow.kale.irc.prefix

import chat.willow.kale.irc.CharacterCodes

object PrefixParser : IPrefixParser {

    override fun parse(rawPrefix: String): Prefix? {
        var raw = rawPrefix

        if (raw.isEmpty()) {
            return null
        }

        var nick = raw
        var user: String? = null
        var host: String? = null

        val indexOfLastAt = rawPrefix.indexOfLast { character -> character == CharacterCodes.AT }
        if (indexOfLastAt >= 0) {
            raw = rawPrefix.substring(0, indexOfLastAt)
            nick = raw

            if (rawPrefix.length > indexOfLastAt + 2) {
                host = rawPrefix.substring(indexOfLastAt + 1, rawPrefix.length)
            } else {
                host = ""
            }
        }

        val indexOfFirstExclam = raw.indexOfFirst { character -> character == CharacterCodes.EXCLAM }
        if (indexOfFirstExclam >= 0) {
            nick = raw.substring(0, indexOfFirstExclam)

            if (raw.length > indexOfFirstExclam + 2) {
                user = raw.substring(indexOfFirstExclam + 1, raw.length)
            } else {
                user = ""
            }
        }

        if (nick.isNullOrEmpty()) {
            return null
        }

        return Prefix(nick = nick, user = user, host = host)
    }

}