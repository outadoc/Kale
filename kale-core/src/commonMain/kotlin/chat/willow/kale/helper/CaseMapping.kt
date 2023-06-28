package chat.willow.kale.helper

import chat.willow.kale.irc.CharacterCodes

interface ICaseMapper {
    val current: CaseMapping

    fun toLower(string: String): String
    override fun toString(): String

}

enum class CaseMapping(val upperToLowerMapping: Map<Char, Char>) {
    ASCII(mapOf()),
    STRICT_RFC1459(mapOf(CharacterCodes.LEFT_STRAIGHT_BRACKET to CharacterCodes.LEFT_CURLY_BRACKET,
                         CharacterCodes.RIGHT_STRAIGHT_BRACKET to CharacterCodes.RIGHT_CURLY_BRACKET)),
    RFC1459(STRICT_RFC1459.upperToLowerMapping.plus(mapOf(CharacterCodes.CARET to CharacterCodes.TILDE)));

    fun toLower(string: String): String {
        val charArray = string.toCharArray()
        for (i in charArray.indices) {
            val replacement = upperToLowerMapping[charArray[i]]
            if (replacement != null) {
                charArray[i] = replacement
            } else {
                charArray[i] = charArray[i].lowercaseChar()
            }
        }

        return String(charArray)
    }
}

fun equalsIgnoreCase(mapping: CaseMapping, lhs: String, rhs: String): Boolean {
    return mapping.toLower(lhs) == mapping.toLower(rhs)
}