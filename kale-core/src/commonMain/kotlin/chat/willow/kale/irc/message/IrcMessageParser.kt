package chat.willow.kale.irc.message

import chat.willow.kale.core.message.IrcMessage
import chat.willow.kale.irc.CharacterCodes

interface IIrcMessageParser {
    fun parse(line: String): IrcMessage?
}

object IrcMessageParser: IIrcMessageParser {

    val MAX_LINE_LENGTH = 8192

    override fun parse(line: String): IrcMessage? {
        if (line.length > MAX_LINE_LENGTH || line.isBlank()) {
            return null
        }

        val (tags, endOfTags) = parseTags(line, 0) ?: return null

        if (endOfTags >= line.length) {
            // tags but no more
            return null
        }

        val (prefix, endOfPrefix) = parsePrefix(line, endOfTags) ?: return null

        if (endOfPrefix >= line.length) {
            // prefix but no more
            return null
        }

        val (command, endOfCommand) = parseCommand(line, endOfPrefix) ?: return null

        if (command.isEmpty()) {
            return null
        }

        if (endOfCommand >= line.length) {
            return IrcMessage(tags, prefix, command)
        }

        val (parameters, @Suppress("UNUSED_VARIABLE") endOfParameters) = parseParameters(line, endOfCommand) ?: return null

        return IrcMessage(tags, prefix, command, parameters)
    }

    private fun parseTags(line: String, fromPosition: Int): Pair<Map<String, String?>, Int>? {
        var position = fromPosition

        if (position >= line.length) {
            return null
        }

        if (line[position] == CharacterCodes.AT) {
            position++

            val nextSpace = ParseHelper.findNext(line, position, CharacterCodes.SPACE) ?: return null
            if (nextSpace <= 0) {
                // @ but no tags
                return null
            }

            val unparsedTags = line.substring(position, nextSpace)
            val tags = ParseHelper.parseToKeysAndOptionalValues(unparsedTags, CharacterCodes.SEMICOLON, CharacterCodes.EQUALS) {
                it.replace("\\:", CharacterCodes.SEMICOLON.toString())
                  .replace("\\s", CharacterCodes.SPACE.toString())
                  .replace("\\\\", CharacterCodes.BACKSLASH.toString())
                  .replace("\\r", CharacterCodes.CR.toString())
                  .replace("\\n", CharacterCodes.LF.toString())
            }

            position = ParseHelper.skipSpaces(line, nextSpace + 1)
            return Pair(tags, position)
        }

        return Pair(emptyMap(), position)
    }

    private fun parsePrefix(line: String, fromPosition: Int): Pair<String?, Int>? {
        var position = fromPosition

        if (position >= line.length) {
            return null
        }

        if (line[position] == CharacterCodes.COLON) {
            position++

            val nextSpace = ParseHelper.findNext(line, position, CharacterCodes.SPACE) ?: return null
            if (nextSpace < position + 1) {
                // : but nothing else
                return null
            }

            val prefix = line.substring(position, nextSpace)

            position = ParseHelper.skipSpaces(line, nextSpace + 1)

            return Pair(prefix, position)
        }

        return Pair(null, position)
    }

    private fun parseCommand(line: String, fromPosition: Int): Pair<String, Int>? {
        var position = fromPosition

        if (position >= line.length) {
            return null
        }

        val nextSpace = ParseHelper.findNext(line, position, CharacterCodes.SPACE)

        val command: String

        if (nextSpace == null) {
            command = line.substring(position)
            if (command.isEmpty()) {
                return null
            }

            position += command.length
        } else {
            if (nextSpace < position + 1) {
                return null
            }

            command = line.substring(position, nextSpace)

            position = ParseHelper.skipSpaces(line, nextSpace + 1)
        }

        return Pair(command, position)
    }

    private fun parseParameters(line: String, fromPosition: Int): Pair<List<String>, Int>? {
        var position = fromPosition
        val parameters = mutableListOf<String>()

        while (position < line.length) {
            if (line[position] == CharacterCodes.COLON) {
                position++

                if (position >= line.length) {
                    parameters.add("")
                } else {
                    parameters.add(line.substring(position))
                }

                return Pair(parameters, line.length)
            }

            val nextSpace = ParseHelper.findNext(line, position, CharacterCodes.SPACE)
            position = if (nextSpace != null) {
                val parameter = line.substring(position, nextSpace)
                parameters.add(parameter)

                ParseHelper.skipSpaces(line, nextSpace + 1)
            } else {
                val parameter = line.substring(position)
                parameters.add(parameter)

                line.length
            }
        }

        return Pair(parameters, position)
    }

}

object ParseHelper {

    fun parseToKeysAndOptionalValues(string: String, chunkSeparator: Char, keyValueSeparator: Char, valueTransform: ((String) -> (String))? = null): Map<String, String?> {
        val keyValues = mutableMapOf<String, String?>()

        val unparsedChunks = string.split(chunkSeparator).filterNot(String::isEmpty)
        for (chunk in unparsedChunks) {
            val nextEquals = findNext(chunk, 0, keyValueSeparator)
            if (nextEquals != null) {
                val key = chunk.substring(0, nextEquals)
                val value: String?

                value = if (nextEquals + 1 >= chunk.length) {
                    // key but no value
                    ""
                } else {
                    val rawValue = chunk.substring(nextEquals + 1, chunk.length)
                    if (valueTransform != null) {
                        valueTransform(rawValue)
                    } else {
                        rawValue
                    }
                }

                keyValues.put(key, value)
            } else {
                keyValues.put(chunk, null)
            }
        }

        return keyValues
    }

    fun findNext(line: String, fromPosition: Int, character: Char): Int? {
        val nextSpacePosition = line.indexOf(character, fromPosition)

        return if (nextSpacePosition >= 0) {
            nextSpacePosition
        } else {
            null
        }
    }

    fun skipSpaces(line: String, fromPosition: Int): Int {
        var position = fromPosition

        while (position < line.length && line[position] == CharacterCodes.SPACE) {
            position++
        }

        return position
    }

}