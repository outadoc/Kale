package engineer.carrot.warren.kale.irc.message.extension.cap

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CapDelMessageTests {

    private lateinit var factory: CapDelMessage.Factory

    @Before fun setUp() {
        factory = CapDelMessage
    }

    @Test fun test_parse_SingleCap() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "DEL", "cap1 ")))

        assertEquals(CapDelMessage(target = "test-nick", caps = listOf("cap1")), message)
    }

    @Test fun test_parse_MultipleCaps() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "DEL", "cap1 cap2 cap3")))

        assertEquals(CapDelMessage(target = "test-nick", caps = listOf("cap1", "cap2", "cap3")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "CAP", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick")))
        val messageThree = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "DEL")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_SingleCap() {
        val message = factory.serialise(CapDelMessage(target = "someone", caps = listOf("cap1")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("someone", "DEL", "cap1")), message)
    }

    @Test fun test_serialise_MultipleCaps() {
        val message = factory.serialise(CapDelMessage(target = "someone", caps = listOf("cap1", "cap2", "cap3")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("someone", "DEL", "cap1 cap2 cap3")), message)
    }

}