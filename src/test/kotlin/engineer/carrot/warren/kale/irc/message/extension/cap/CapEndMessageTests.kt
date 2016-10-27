package engineer.carrot.warren.kale.irc.message.extension.cap

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CapEndMessageTests {
    lateinit var factory: CapEndMessage.Factory

    @Before fun setUp() {
        factory = CapEndMessage
    }

    @Test fun test_parse() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "END")))

        assertEquals(CapEndMessage(target = "test-nick"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "CAP", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick")))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise() {
        val message = factory.serialise(CapEndMessage())

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("END")), message)
    }

}