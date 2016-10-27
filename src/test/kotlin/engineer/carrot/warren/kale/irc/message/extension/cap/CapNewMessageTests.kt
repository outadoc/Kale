package engineer.carrot.warren.kale.irc.message.extension.cap

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CapNewMessageTests {

    private lateinit var factory: CapNewMessage.Factory

    @Before fun setUp() {
        factory = CapNewMessage
    }

    @Test fun test_parse_SingleCap() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NEW", "cap1 ")))

        assertEquals(CapNewMessage(target = "test-nick", caps = mapOf("cap1" to null)), message)
    }

    @Test fun test_parse_MultipleCaps() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NEW", "cap1 cap2=value cap3=")))

        assertEquals(CapNewMessage(target = "test-nick", caps = mapOf("cap1" to null, "cap2" to "value", "cap3" to "")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "CAP", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick")))
        val messageThree = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NEW")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_SingleCap() {
        val message = factory.serialise(CapNewMessage(target = "someone", caps = mapOf("cap1" to null)))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("someone", "NEW", "cap1")), message)
    }

    @Test fun test_serialise_MultipleCaps() {
        val message = factory.serialise(CapNewMessage(target = "someone", caps = mapOf("cap1" to null, "cap2" to "", "cap3" to "val3")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("someone", "NEW", "cap1 cap2= cap3=val3")), message)
    }

}