package engineer.carrot.warren.kale.irc.message.rfc1459

import com.google.common.base.Strings
import com.google.common.collect.Lists
import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import engineer.carrot.warren.kale.irc.prefix.PrefixParser
import engineer.carrot.warren.kale.irc.prefix.PrefixSerialiser
import java.util.*

data class ModeMessage(val source: Prefix? = null, val target: String, val modifiers: List<ModeModifier>): IMessage {

    data class ModeModifier(val type: Char? = null, val mode: Char, var parameter: String? = null) {
        val isAdding: Boolean
            get() = this.type == CharacterCodes.PLUS

        val isRemoving: Boolean
            get() = this.type == CharacterCodes.MINUS

        val isListing: Boolean
            get() = this.type == null
    }

    companion object Factory: IMessageFactory<ModeMessage> {
        override val messageType = ModeMessage::class.java
        override val command = "MODE"

        override fun serialise(message: ModeMessage): IrcMessage? {
            val prefix = if (message.source != null) { PrefixSerialiser.serialise(message.source) } else { null }
            val parameters = serialise(message.modifiers).toMutableList()
            parameters.add(0, message.target)

            return IrcMessage(command = command, prefix = prefix, parameters = parameters)
        }

        private fun serialise(modifiers: List<ModeModifier>): List<String> {
            var parameters: MutableList<String> = mutableListOf()

            for (modifier in modifiers) {
                if (modifier.type != null) {
                    parameters.add("${modifier.type}${modifier.mode}")
                } else {
                    parameters.add("${modifier.mode}")
                }

                val parameter = modifier.parameter
                if (parameter != null) {
                    parameters.add(parameter)
                }
            }

            return parameters
        }

        override fun parse(message: IrcMessage): ModeMessage? {
            if (message.parameters.size < 1) {
                return null
            }

            val source = PrefixParser.parse(message.prefix ?: "")
            val target = message.parameters[0]

            // FIXME: distinguish between channel mode message and user mode message using state delegate

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

        private data class ModeChunk(val modes: String, val parameters: Queue<String> = LinkedList()) { }

        private fun parseIrcParameters(parameters: List<String>): List<ModeModifier> {
            val chunks = this.parseParametersToModeChunks(parameters)
            return this.parseChunksToModifiers(chunks)
        }

        private fun isTokenStartOfModifier(token: Char?): Boolean {
            return token === CharacterCodes.PLUS || token === CharacterCodes.MINUS

        }

        private fun parseParametersToModeChunks(parameters: List<String>): List<ModeChunk> {
            val chunks = Lists.newArrayList<ModeChunk>()

            if (parameters.isEmpty()) {
                return chunks
            }

            var currentChunk: ModeChunk? = null

            for (parameter in parameters) {
                if (parameter.isEmpty()) {
                    println("Attempted to parse an empty parameter in to a chunk - bailing")

                    break
                }

                if (this.isTokenStartOfModifier(parameter[0])) {
                    currentChunk = ModeChunk(parameter)
                    chunks.add(currentChunk)

                    continue
                }

                if (currentChunk == null) {
                    println("Attempted to add a chunk without having a type token first - bailing")

                    break
                }

                currentChunk.parameters.add(parameter)
            }

            return chunks
        }

        private fun parseChunksToModifiers(chunks: List<ModeChunk>): List<ModeModifier> {
            val modifiers = Lists.newArrayList<ModeModifier>()

            var currentType: Char? = null

            for (chunk in chunks) {
                for (token in this.parseModes(chunk.modes)) {
                    if (this.isTokenStartOfModifier(token)) {
                        currentType = token

                        continue
                    }

                    if (currentType == null) {
                        println("Tried to add a modifier that didn't start with +- - bailing: '$token'")

                        continue
                    }

                    val modifier = ModeModifier(type = currentType, mode = token)

                    val isAdding = currentType === CharacterCodes.PLUS
                    val takesAParameter = this.takesAParameter(isAdding, token)

                    if (takesAParameter) {
                        val parameter = chunk.parameters.poll()

                        if (Strings.isNullOrEmpty(parameter)) {
                            println("MODE modifier was missing an expected parameter - not processing it: '$token'")

                            continue
                        }

                        modifier.parameter = parameter
                    }

                    modifiers.add(modifier)
                }

                if (!chunk.parameters.isEmpty()) {
                    println("Chunk had parameters left after polling - something probably went wrong!")
                }
            }

            return modifiers
        }

        private fun parseModes(token: String): List<Char> {
            val modes = Lists.newArrayList<Char>()

            for (i in 0..token.length - 1) {
                modes.add(token[i])
            }

            return modes
        }

        private fun takesAParameter(isAdding: Boolean, token: Char): Boolean {
            // FIXME: tie in to ISUPPORT

            if (isAdding) {
                return plusRequiringParameter.contains(token)
            } else {
                return minusRequiringParameter.contains(token)
            }
        }

        private val plusRequiringParameter: Set<Char> = setOf('o', 'v', 'h', 'b', 'l', 'k', 'q', 'e', 'I')
        private val minusRequiringParameter: Set<Char> = setOf('o', 'v', 'h', 'b', 'k', 'q', 'e', 'I')
    }

}