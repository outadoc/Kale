package engineer.carrot.warren.kale

import com.nhaarman.mockito_kotlin.mock
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KaleRouterTests {

    lateinit var sut: KaleRouter

    @Before fun setUp() {
        sut = KaleRouter()
    }

    @Test fun test_parserFor_AfterRegisteringCommand_ReturnsSameParser() {
        val parser = mock<IMessageParser<IMessage>>()
        val message = IrcMessage(command = "TEST")

        sut.routeCommandToParser(message.command, parser)

        val returnedParser = sut.parserFor(message)

        assertTrue(parser === returnedParser)
    }

    @Test fun test_parserFor_ForMissingCommand_ReturnsNull() {
        val parser = mock<IMessageParser<IMessage>>()
        val message = IrcMessage(command = "TEST")

        sut.routeCommandToParser(message.command, parser)

        val returnedParser = sut.parserFor(IrcMessage(command = "NOT TEST"))

        assertNull(returnedParser)
    }

    @Test fun test_parserFor_AfterRegisteringParserMatcher_ReturnsCorrectParser() {
        val parser = mock<IMessageParser<IMessage>>()
        val message = IrcMessage(command = "TEST")
        val matcher = object : ParserMatcher {
            override fun invoke(invokeMessage: IrcMessage): IMessageParser<*>? {
                return parser
            }
        }

        sut.routeCommandToParserMatcher(message.command, matcher)

        val returnedParser = sut.parserFor(message)

        assertTrue(parser === returnedParser)
    }

    @Test fun test_parserFor_AfterRegisteringParserMatcher_ForMissingCommand_ReturnsNull() {
        val parser = mock<IMessageParser<IMessage>>()
        val message = IrcMessage(command = "TEST")
        val matcher = object : ParserMatcher {
            override fun invoke(invokeMessage: IrcMessage): IMessageParser<*>? {
                return parser
            }
        }

        sut.routeCommandToParserMatcher(message.command, matcher)

        val returnedParser = sut.parserFor(IrcMessage(command = "NOT TEST"))

        assertNull(returnedParser)
    }

    @Test fun test_serialiserFor_AfterRoutingMessageToSerialiser_ReturnsCorrectSerialiser() {
        val serialiser = mock<IMessageSerialiser<IMessage>>()
        val message = mock<IMessage>()

        sut.routeMessageToSerialiser(message::class, serialiser)

        val returnedSerialiser = sut.serialiserFor(message::class.java)

        assertTrue(serialiser === returnedSerialiser)
    }

    @Test fun test_serialiserFor_AfterRoutingMessageToSerialiser_ForMissingClass_ReturnsNull() {
        val message = mock<IMessage>()

        val returnedSerialiser = sut.serialiserFor(message::class.java)

        assertNull(returnedSerialiser)
    }

}