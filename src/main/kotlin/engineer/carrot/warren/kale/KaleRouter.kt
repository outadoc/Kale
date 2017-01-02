package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import kotlin.reflect.KClass

typealias ParserMatcher = (IrcMessage) -> (IMessageParser<*>?)

interface IParserRouter {

    fun parserFor(message: IrcMessage): IMessageParser<*>?

}

interface IKaleRouter {

    fun <M: IMessage> routeCommandToParser(command: String, parser: IMessageParser<M>)
    fun routeCommandToParserMatcher(command: String, matcher: ParserMatcher)
    fun parserFor(ircMessage: IrcMessage): IMessageParser<*>?

    fun <M: Any> routeMessageToSerialiser(messageClass: KClass<M>, serialiser: IMessageSerialiser<M>)
    fun <M: Any> serialiserFor(messageClass: Class<M>): IMessageSerialiser<M>?

}

class KaleRouter : IKaleRouter {

    private val commandsToParsers = hashMapOf<String, IParserRouter>()
    private val messagesToSerialisers = hashMapOf<Class<*>, IMessageSerialiser<*>>()

    override fun <T: IMessage> routeCommandToParser(command: String, parser: IMessageParser<T>) {
        val parserRouter = object : IParserRouter {
            override fun parserFor(message: IrcMessage): IMessageParser<*>? {
                return parser
            }
        }

        commandsToParsers[command] = parserRouter
    }

    override fun routeCommandToParserMatcher(command: String, matcher: ParserMatcher) {
        commandsToParsers[command] = object : IParserRouter {
            override fun parserFor(message: IrcMessage): IMessageParser<*>? {
                return matcher(message)
            }
        }
    }

    override fun parserFor(ircMessage: IrcMessage): IMessageParser<*>? {
        return commandsToParsers[ircMessage.command]?.parserFor(ircMessage)
    }

    override fun <M : Any> routeMessageToSerialiser(messageClass: KClass<M>, serialiser: IMessageSerialiser<M>) {
        messagesToSerialisers[messageClass.java] = serialiser
    }

    override fun <M: Any> serialiserFor(messageClass: Class<M>): IMessageSerialiser<M>? {
        @Suppress("UNCHECKED_CAST")
        return messagesToSerialisers[messageClass] as? IMessageSerialiser<M>
    }

}