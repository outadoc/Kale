package engineer.carrot.warren.kale.irc.message

import engineer.carrot.warren.kale.irc.CharacterCodes

interface IIrcMessageParser {
    fun parse(line: String): IrcMessage?
}

object IrcMessageParser: IIrcMessageParser {

    private val CRLF_LENGTH = 2
    val MAX_LINE_LENGTH = 1024 - CRLF_LENGTH
    val MIN_LINE_LENGTH = 5 - CRLF_LENGTH

    override fun parse(line: String): IrcMessage? {
        if (line.length > MAX_LINE_LENGTH || line.length < MIN_LINE_LENGTH) {
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

        val nextSpace = ParseHelper.findNext(line, position, CharacterCodes.SPACE)

        val command: String

        if (nextSpace == null) {
            command = line.substring(position)
            if (command.length <= 0) {
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
            if (nextSpace != null) {
                val parameter = line.substring(position, nextSpace)
                parameters.add(parameter)

                position = ParseHelper.skipSpaces(line, nextSpace + 1)
            } else {
                val parameter = line.substring(position)
                parameters.add(parameter)

                position = line.length
            }
        }

        return Pair(parameters, position)
    }

}

object ParseHelper {

    fun parseToKeysAndOptionalValues(string: String, chunkSeparator: Char, keyValueSeparator: Char, valueTransform: ((String) -> (String))? = null): Map<String, String?> {
        val keyValues = mutableMapOf<String, String?>()

        val unparsedChunks = string.split(delimiters = chunkSeparator).filterNot { it.isEmpty() }
        for (chunk in unparsedChunks) {
            val nextEquals = ParseHelper.findNext(chunk, 0, keyValueSeparator)
            if (nextEquals != null) {
                val key = chunk.substring(0, nextEquals)
                val value: String?

                if (nextEquals + 1 >= chunk.length) {
                    // key but no value
                    value = ""
                } else {
                    val rawValue = chunk.substring(nextEquals + 1, chunk.length)
                    if (valueTransform != null) {
                        value = valueTransform(rawValue)
                    } else {
                        value = rawValue
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

        if (nextSpacePosition >= 0) {
            return nextSpacePosition
        } else {
            return null
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