package engineer.carrot.warren.kale.irc.message

import engineer.carrot.warren.kale.irc.CharacterCodes

object IrcMessageSerialiser : IIrcMessageSerialiser {

    override fun serialise(message: IrcMessage): String? {
        val builder = StringBuilder()

        if (message.tags.isNotEmpty()) {
            val tags = SerialiserHelper.serialiseKeysAndOptionalValues(message.tags, CharacterCodes.EQUALS, CharacterCodes.SEMICOLON)

            builder.append(CharacterCodes.AT)
            builder.append(tags)
            builder.append(CharacterCodes.SPACE)
        }

        if (message.prefix != null) {
            builder.append(CharacterCodes.COLON)
            builder.append(message.prefix)
            builder.append(CharacterCodes.SPACE)
        }

        builder.append(message.command)

        if (message.parameters.isNotEmpty()) {
            builder.append(CharacterCodes.SPACE)

            val parametersSize = message.parameters.size

            if (message.parameters.size > 1) {
                for (i in 0 .. parametersSize - 2) {
                    builder.append(message.parameters[i])
                    builder.append(CharacterCodes.SPACE)
                }
            }

            builder.append(CharacterCodes.COLON)
            builder.append(message.parameters[parametersSize - 1])
        }

        val output = builder.toString()
        if (output.length < IrcMessageParser.MIN_LINE_LENGTH || output.length > IrcMessageParser.MAX_LINE_LENGTH) {
            println("serialised message is too long: $output")
            return null
        }

        return output
    }

}

object SerialiserHelper {

    fun serialiseKeysAndOptionalValues(keyValues: Map<String, String?>, keyValueSeparator: Char, chunkSeparator: Char): String {
        val serialisedKeyValues = mutableListOf<String>()

        for ((key, value) in keyValues) {
            if (value == null) {
                serialisedKeyValues.add(key)
            } else {
                serialisedKeyValues.add("$key$keyValueSeparator$value")
            }
        }

        return serialisedKeyValues.filterNotNull().joinToString(separator = chunkSeparator.toString())
    }

}