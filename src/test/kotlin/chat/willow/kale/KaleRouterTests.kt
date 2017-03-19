package chat.willow.kale

import chat.willow.kale.irc.message.IMessageSerialiser
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KaleRouterTests {

    private lateinit var sut: KaleRouter
    private lateinit var handlerOne: IKaleIrcMessageHandler
    private lateinit var handlerTwo: IKaleIrcMessageHandler

    @Before fun setUp() {
        handlerOne = mock()
        handlerTwo = mock()

        sut = KaleRouter()
    }

    @Test fun test_handlerFor_CommandNotRegistered_ReturnsNull() {
        val handler = sut.handlerFor("COMMAND")

        assertNull(handler)
    }

    @Test fun test_handlerFor_CommandRegistered_ReturnsCorrectHandler() {
        sut.register("1", handlerOne)
        sut.register("2", handlerTwo)

        val handler = sut.handlerFor("1")

        assertTrue(handlerOne === handler)
    }

    @Test fun test_handlerFor_AfterReRegisteringCommand_WithDifferentHandler_ReturnsNewHandler() {
        val newHandlerOne: IKaleIrcMessageHandler = mock()
        sut.register("1", handlerOne)
        sut.register("2", handlerTwo)
        sut.register("1", newHandlerOne)

        val handler = sut.handlerFor("1")

        assertTrue(newHandlerOne === handler)
    }

    @Test fun test_serialiserFor_MessageNotRegistered_ReturnsNull() {
        val serialiser = sut.serialiserFor(Int::class.java)

        assertNull(serialiser)
    }

    @Test fun test_serialiserFor_MessageRegistered_ReturnsCorrectSerialiser() {
        val serialiserOne: IMessageSerialiser<Int> = mock()
        sut.register(Int::class, serialiserOne)

        val serialiser = sut.serialiserFor(Int::class.java)

        assertTrue(serialiserOne === serialiser)
    }

    @Test fun test_serialiserFor_DifferentMessageRegistered_ReturnsCorrectSerialiser() {
        val serialiserOne: IMessageSerialiser<Int> = mock()
        val serialiserTwo: IMessageSerialiser<String> = mock()
        val newSerialiserOne: IMessageSerialiser<Int> = mock()
        sut.register(Int::class, serialiserOne)
        sut.register(String::class, serialiserTwo)
        sut.register(Int::class, newSerialiserOne)

        val serialiser = sut.serialiserFor(Int::class.java)

        assertTrue(newSerialiserOne === serialiser)
    }

    // TODO: Test client defaults?

}