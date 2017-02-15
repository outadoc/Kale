package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.IKaleParsingStateDelegate
import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.PrefixParser
import chat.willow.kale.irc.prefix.PrefixSerialiser
import chat.willow.kale.loggerFor
import java.util.*

data class ModeMessage(val source: Prefix? = null, val target: String, val modifiers: List<ModeModifier>): IMessage {
    override val command: String = "MODE"

    data class ModeModifier(val type: Char? = null, val mode: Char, var parameter: String? = null) {
        @Suppress("UNUSED") val isAdding: Boolean
            get() = this.type == CharacterCodes.PLUS

        @Suppress("UNUSED") val isRemoving: Boolean
            get() = this.type == CharacterCodes.MINUS

        @Suppress("UNUSED") val isListing: Boolean
            get() = this.type == null
    }

    companion object Factory: IMessageParser<ModeMessage>, IMessageSerialiser<ModeMessage> {
        val LOGGER = loggerFor<Factory>()

        var parsingStateDelegate: IKaleParsingStateDelegate? = null

        override fun serialise(message: ModeMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }
            val parameters = serialise(message.modifiers).toMutableList()
            parameters.add(0, message.target)

            return IrcMessage(command = message.command, prefix = prefix, parameters = parameters)
        }

        private fun serialise(modifiers: List<ModeModifier>): List<String> {
            val parameters = mutableListOf<String>()

            for ((type, mode, parameter) in modifiers) {
                if (type != null) {
                    parameters.add("$type$mode")
                } else {
                    parameters.add("$mode")
                }

                if (parameter != null) {
                    parameters.add(parameter)
                }
            }

            return parameters
        }

        override fun parse(message: IrcMessage): ModeMessage? {
            if (message.parameters.isEmpty()) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val target = message.parameters[0]

            if (message.parameters.size >= 2) {
                val secondParameter = message.parameters[1]
                if (!secondParameter.isEmpty() && !isTokenStartOfModifier(secondParameter[0])) {
                    // special case
                    // assume that the message is for listing modes
                    //  example: MODE #meditation e

                    val modifier = ModeModifier(mode = secondParameter[0])

                    return ModeMessage(target = target, source = source, modifiers = listOf(modifier))
                }

                val remainingParameters = message.parameters.subList(1, message.parameters.size)
                val modifiers = parseIrcParameters(remainingParameters)

                return ModeMessage(target = target, source = source, modifiers = modifiers)
            } else {
                return null
            }
        }

        private data class ModeChunk(val modes: String, val parameters: Queue<String> = LinkedList())

        private fun parseIrcParameters(parameters: List<String>): List<ModeModifier> {
            val chunks = this.parseParametersToModeChunks(parameters)
            return this.parseChunksToModifiers(chunks)
        }

        private fun isTokenStartOfModifier(token: Char?): Boolean {
            return token != null && (token == CharacterCodes.PLUS || token == CharacterCodes.MINUS)

        }

        private fun parseParametersToModeChunks(parameters: List<String>): List<ModeChunk> {
            val chunks = mutableListOf<ModeChunk>()

            if (parameters.isEmpty()) {
                return chunks
            }

            var currentChunk: ModeChunk? = null

            for (parameter in parameters) {
                if (parameter.isEmpty()) {
                    LOGGER.warn("Attempted to parse an empty parameter in to a chunk - bailing")

                    break
                }

                if (this.isTokenStartOfModifier(parameter[0])) {
                    currentChunk = ModeChunk(parameter)
                    chunks.add(currentChunk)

                    continue
                }

                if (currentChunk == null) {
                    LOGGER.warn("Attempted to add a chunk without having a type token first - bailing")

                    break
                }

                currentChunk.parameters.add(parameter)
            }

            return chunks
        }

        private fun parseChunksToModifiers(chunks: List<ModeChunk>): List<ModeModifier> {
            val modifiers = mutableListOf<ModeModifier>()

            var currentType: Char? = null

            for (chunk in chunks) {
                for (token in this.parseModes(chunk.modes)) {
                    if (this.isTokenStartOfModifier(token)) {
                        currentType = token

                        continue
                    }

                    if (currentType == null) {
                        LOGGER.warn("Tried to add a modifier that didn't start with +- - bailing: '$token'")

                        continue
                    }

                    val modifier = ModeModifier(type = currentType, mode = token)

                    val isAdding = currentType == CharacterCodes.PLUS
                    val takesAParameter = this.takesAParameter(isAdding, token)

                    if (takesAParameter) {
                        val parameter = chunk.parameters.poll()

                        if (parameter.isNullOrEmpty()) {
                            LOGGER.warn("MODE modifier was missing an expected parameter - not processing it: '$token'")

                            continue
                        }

                        modifier.parameter = parameter
                    }

                    modifiers.add(modifier)
                }

                if (!chunk.parameters.isEmpty()) {
                    LOGGER.warn("Chunk had parameters left after polling - something probably went wrong!")
                }
            }

            return modifiers
        }

        private fun parseModes(token: String): List<Char> {
            val modes = (0..token.length - 1).map { token[it] }

            return modes
        }

        private fun takesAParameter(isAdding: Boolean, token: Char): Boolean {
            val delegateTakesAParameter = parsingStateDelegate?.modeTakesAParameter(isAdding, token)
            return delegateTakesAParameter ?: if (isAdding) {
                return defaultPlusRequiringParameter.contains(token)
            } else {
                return defaultMinusRequiringParameter.contains(token)
            }
        }

        private val defaultPlusRequiringParameter: Set<Char> = setOf('o', 'v', 'h', 'b', 'l', 'k', 'q', 'e', 'I')
        private val defaultMinusRequiringParameter: Set<Char> = setOf('o', 'v', 'h', 'b', 'k', 'q', 'e', 'I')
    }

}