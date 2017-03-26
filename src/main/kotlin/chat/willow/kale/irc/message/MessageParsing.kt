package chat.willow.kale.irc.message

import chat.willow.kale.IrcMessageComponents

interface IMessageParser<out T> {

    fun parse(message: IrcMessage): T?

}

interface IComponentsParser<out T> {

    fun parseFromComponents(components: IrcMessageComponents): T?

}

abstract class MessageParser<out T>(private val command: String) : IMessageParser<T>, IComponentsParser<T> {

    override fun parse(message: IrcMessage): T? {
        if (message.command != command) {
            return null
        }

        val components = IrcMessageComponents(tags = message.tags, prefix = message.prefix, parameters = message.parameters)

        return parseFromComponents(components)
    }

}

abstract class SubcommandParser<out T>(private val subcommand: String, private val subcommandPosition: Int = 0) : IMessageParser<T> {

    override fun parse(message: IrcMessage): T? {
        if (message.parameters.size <= subcommand) {
            return null
        }

        if (message.parameters.getOrNull(subcommandPosition) != subcommand) {
            return null
        }

        val parameters = message.parameters.filterIndexed {
            index, _ -> index != subcommandPosition
        }

        val components = IrcMessageComponents(tags = message.tags, prefix = message.prefix, parameters = parameters)

        return parseFromComponents(components)
    }

    abstract protected fun parseFromComponents(components: IrcMessageComponents): T?

}

abstract class PrefixSubcommandParser<out T>(private val token: String, private val subcommandPosition: Int = 0) : IMessageParser<T> {

    override fun parse(message: IrcMessage): T? {
        if (message.parameters.isEmpty() || message.parameters.size <= subcommandPosition) {
            return null
        }

        val subcommandStartsWithToken = message.parameters.getOrNull(subcommandPosition)?.startsWith(token) ?: false
        if (!subcommandStartsWithToken) {
            return null
        }

        val parameters = message.parameters.toMutableList()
        parameters[subcommandPosition] = parameters[subcommandPosition].removePrefix(token)

        val components = IrcMessageComponents(tags = message.tags, prefix = message.prefix, parameters = parameters)

        return parseFromComponents(components)
    }

    abstract protected fun parseFromComponents(components: IrcMessageComponents): T?

}
