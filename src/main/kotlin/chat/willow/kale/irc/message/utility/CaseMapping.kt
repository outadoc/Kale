package chat.willow.kale.irc.message.utility

import chat.willow.kale.irc.CharacterCodes

enum class CaseMapping(val upperToLowerMapping: Map<Char, Char>) {
    ASCII(mapOf()),
    STRICT_RFC1459(mapOf(CharacterCodes.LEFT_STRAIGHT_BRACKET to CharacterCodes.LEFT_CURLY_BRACKET,
                         CharacterCodes.RIGHT_STRAIGHT_BRACKET to CharacterCodes.RIGHT_CURLY_BRACKET)),
    RFC1459(STRICT_RFC1459.upperToLowerMapping.plus(mapOf(CharacterCodes.CARET to CharacterCodes.TILDE)));

    fun toLower(string: String): String {
        val charArray = string.toCharArray()
        for (i in 0..charArray.size-1) {
            val replacement = upperToLowerMapping[charArray[i]]
            if (replacement != null) {
                charArray[i] = replacement
            } else {
                charArray[i] = charArray[i].toLowerCase()
            }
        }

        return String(charArray)
    }
}

fun equalsIgnoreCase(mapping: CaseMapping, lhs: String, rhs: String): Boolean {
    return mapping.toLower(lhs) == mapping.toLower(rhs)
}