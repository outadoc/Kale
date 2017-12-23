package chat.willow.kale.core.message

interface IMessageSerialiser<in T> {

    fun serialise(message: T): IrcMessage?

}

interface IComponentsSerialiser<in T> {

    fun serialiseToComponents(message: T): IrcMessageComponents

}

abstract class MessageSerialiser<in T>(private val command: String) : IMessageSerialiser<T>, IComponentsSerialiser<T> {

    override fun serialise(message: T): IrcMessage? {
        val components = serialiseToComponents(message)

        return IrcMessage(command = command, tags = components.tags, prefix = components.prefix, parameters = components.parameters)
    }

}

abstract class SubcommandSerialiser<in T>(private val command: String, private val subcommand: String, private val subcommandPosition: Int = 0) : IMessageSerialiser<T> {

    override fun serialise(message: T): IrcMessage? {
        val components = serialiseToComponents(message)

        val parameters = components.parameters.toMutableList()
        parameters.add(subcommandPosition, subcommand)

        return IrcMessage(command = command, tags = components.tags, prefix = components.prefix, parameters = parameters)
    }

    abstract protected fun serialiseToComponents(message: T): IrcMessageComponents

}

abstract class PrefixSubcommandSerialiser<in T>(private val command: String, private val token: String, private val subcommandPosition: Int = 0) : IMessageSerialiser<T> {

    override fun serialise(message: T): IrcMessage? {
        val components = serialiseToComponents(message)

        val parameters = components.parameters.toMutableList()
        parameters[subcommandPosition] = token + parameters[subcommandPosition]

        return IrcMessage(command = command, tags = components.tags, prefix = components.prefix, parameters = parameters)
    }

    abstract protected fun serialiseToComponents(message: T): IrcMessageComponents

}