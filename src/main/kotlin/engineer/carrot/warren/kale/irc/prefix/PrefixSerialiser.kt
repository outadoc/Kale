package engineer.carrot.warren.kale.irc.prefix

import engineer.carrot.warren.kale.irc.CharacterCodes

object PrefixSerialiser: IPrefixSerialiser {

    override fun serialise(prefix: Prefix): String {
        var serialisedPrefix = prefix.nick

        if (prefix.user != null) {
            serialisedPrefix += CharacterCodes.EXCLAM + prefix.user
        }

        if (prefix.host != null) {
            serialisedPrefix += CharacterCodes.AT + prefix.host
        }

        return serialisedPrefix
    }

}