package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.message.IrcMessageParser
import engineer.carrot.warren.kale.irc.message.rfc1459.*

class Kale : IKale {
    private var messageFactories: MutableMap<String, IMessageFactory<*>> = hashMapOf()
    private var messageToFactory: MutableMap<Class<*>, IMessageFactory<*>> = hashMapOf()

    var handlers: MutableMap<String, IKaleHandler<*>> = hashMapOf()

    fun addDefaultMessages(): Kale {
        addMessageFromFactory(PingMessage.Factory)
        addMessageFromFactory(PongMessage.Factory)
        addMessageFromFactory(NickMessage.Factory)
        addMessageFromFactory(UserMessage.Factory)
        addMessageFromFactory(QuitMessage.Factory)
        addMessageFromFactory(JoinMessage.Factory)
        addMessageFromFactory(PartMessage.Factory)
        addMessageFromFactory(ModeMessage.Factory)

        return this
    }

    fun <T: IMessage> addMessageFromFactory(factory: IMessageFactory<T>) {
        if (messageFactories.containsKey(factory.command)) {
            throw RuntimeException("Tried to add message factory for ${factory.command}, but one already exists!")
        }

        messageFactories.put(factory.command, factory)
        messageToFactory.put(factory.messageType, factory)
    }

    private fun factoryFromMessage(message: Class<*>): IMessageFactory<*>? {
        return messageToFactory[message]
    }

    override fun <T: IMessage> register(handler: IKaleHandler<T>) {
        val command = factoryFromMessage(handler.messageType)?.command ?: throw RuntimeException("couldn't look up factory for handler: $handler")

        if (handlers.containsKey(command)) {
            throw RuntimeException("tried to register a handler when one already exists for $command: $handler")
        }

        handlers.put(command, handler)
    }

    override fun process(line: String) {
        val ircMessage = IrcMessageParser.parse(line)
        if (ircMessage == null) {
            println("failed to parse line to IrcMessage: $line")
            return
        }

        val factory = messageFactories[ircMessage.command]
        if (factory == null) {
            println("failed to find factory for message parsing: $ircMessage")
            return
        }

        val message = factory.parse(ircMessage)
        if (message == null) {
            println("factory failed to parse message: $factory $ircMessage")
            return
        }

        val handler = handlers[ircMessage.command]
        if (handler == null) {
            println("no handler for: $message")
            return
        }

        @Suppress("UNCHECKED_CAST")
        val typedHandler = handler as? IKaleHandler<IMessage> ?: return
        if (!typedHandler.messageType.isInstance(message)) {
            println("tried to pass wrong type to handler: ${message.javaClass} to ${handler.messageType}")
            return
        }

        typedHandler.handle(message)
    }

    override fun <T: IMessage> serialise(message: T): IrcMessage? {
        @Suppress("UNCHECKED_CAST")
        val factory = factoryFromMessage(message.javaClass) as? IMessageFactory<IMessage>
        if (factory == null) {
            println("failed to find factory for message serialisation: $message")
            return null
        }

        return factory.serialise(message)
    }

}