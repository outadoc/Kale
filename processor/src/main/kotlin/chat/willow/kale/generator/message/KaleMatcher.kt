package chat.willow.kale.generator.message

import chat.willow.kale.generator.tag.ITagStore

typealias KaleMatcher = (IrcMessage) -> Boolean

fun commandMatcher(command: String): KaleMatcher
        = { it.command.equals(command, ignoreCase = true) }

fun subcommandMatcher(command: String, subcommand: String, subcommandPosition: Int = 1): KaleMatcher
        = { it.command.equals(command, ignoreCase = true) && it.parameters.getOrNull(subcommandPosition)?.equals(subcommand, ignoreCase = true) ?: false }

open class KaleDescriptor<out T>(val matcher: KaleMatcher, val parser: IMessageParser<T>)

data class KaleObservable<out T>(val message: T, val meta: ITagStore)