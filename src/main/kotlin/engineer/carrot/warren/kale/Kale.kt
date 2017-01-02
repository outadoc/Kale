package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.*
import engineer.carrot.warren.kale.irc.message.extension.account_notify.AccountMessage
import engineer.carrot.warren.kale.irc.message.extension.away_notify.AwayMessage
import engineer.carrot.warren.kale.irc.message.extension.batch.BatchEndMessage
import engineer.carrot.warren.kale.irc.message.extension.batch.BatchStartMessage
import engineer.carrot.warren.kale.irc.message.extension.cap.*
import engineer.carrot.warren.kale.irc.message.extension.extended_join.ExtendedJoinMessage
import engineer.carrot.warren.kale.irc.message.extension.sasl.AuthenticateMessage
import engineer.carrot.warren.kale.irc.message.extension.sasl.Rpl903Message
import engineer.carrot.warren.kale.irc.message.extension.sasl.Rpl904Message
import engineer.carrot.warren.kale.irc.message.extension.sasl.Rpl905Message
import engineer.carrot.warren.kale.irc.message.rfc1459.*
import engineer.carrot.warren.kale.irc.message.rfc1459.rpl.*
import engineer.carrot.warren.kale.irc.message.utility.RawMessage

interface IKale {

    fun <T: IMessage> register(handler: IKaleHandler<T>)
    fun <T: IMessage> unregister(handler: IKaleHandler<T>)
    fun <M: IMessage> handlerFor(messageClass: Class<M>): IKaleHandler<M>?
    fun process(line: String)
    fun serialise(message: Any): IrcMessage?

    var parsingStateDelegate: IKaleParsingStateDelegate?

}

interface IKaleParsingStateDelegate {

    fun modeTakesAParameter(isAdding: Boolean, token: Char): Boolean

}

class Kale(private val router: IKaleRouter) : IKale {
    private val LOGGER = loggerFor<Kale>()

    override var parsingStateDelegate: IKaleParsingStateDelegate? = null
        set(value) {
            ModeMessage.Factory.parsingStateDelegate = value
        }

    private val handlers = hashMapOf<Class<*>, IKaleHandler<*>>()

    override fun <M: IMessage> register(handler: IKaleHandler<M>) {
        handlers.put(handler.messageType, handler)
    }

    override fun <M: IMessage> unregister(handler: IKaleHandler<M>) {
        handlers.remove(handler.messageType)
    }

    override fun process(line: String) {
        val ircMessage = IrcMessageParser.parse(line)
        if (ircMessage == null) {
            LOGGER.warn("failed to parse line to IrcMessage: $line")
            return
        }

        val factory = router.parserFor(ircMessage)
        if (factory == null) {
            LOGGER.debug("no factory for: $ircMessage")
            return
        }

        val message = factory.parse(ircMessage)
        if (message == null) {
            LOGGER.warn("factory failed to parse message: $factory $ircMessage")
            return
        }

        val handler = findHandlerFor(message)
        if (handler == null) {
            LOGGER.debug("no handler for: $message")
            return
        }

        handler.handle(message, ircMessage.tags)
    }

    private fun <M: IMessage> findHandlerFor(message: M): IKaleHandler<M>? {
        @Suppress("UNCHECKED_CAST")
        return handlers[message.javaClass] as? IKaleHandler<M>
    }

    override fun <M: IMessage> handlerFor(messageClass: Class<M>): IKaleHandler<M>? {
        @Suppress("UNCHECKED_CAST")
        return handlers[messageClass] as? IKaleHandler<M>
    }

    override fun serialise(message: Any): IrcMessage? {
        val factory = router.serialiserFor(message.javaClass)
        if (factory == null) {
            LOGGER.warn("failed to find factory for message serialisation: $message")
            return null
        }

        return factory.serialise(message)
    }

}