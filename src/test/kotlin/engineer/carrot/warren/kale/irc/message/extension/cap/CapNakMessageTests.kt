package engineer.carrot.warren.kale.irc.message.extension.cap

import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.message.extension.cap.CapNakMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CapNakMessageTests {
    lateinit var factory: CapNakMessage.Factory

    @Before fun setUp() {
        factory = CapNakMessage
    }

    @Test fun test_parse_SingleCap() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NAK", "cap1 ")))

        assertEquals(CapNakMessage(target = "test-nick", caps = listOf("cap1")), message)
    }

    @Test fun test_parse_MultipleCaps() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NAK", "cap1 cap2 cap3")))

        assertEquals(CapNakMessage(target = "test-nick", caps = listOf("cap1", "cap2", "cap3")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "CAP", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick")))
        val messageThree = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NAK")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_SingleCap() {
        val message = factory.serialise(CapNakMessage(caps = listOf("cap1")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("NAK", "cap1")), message)
    }

    @Test fun test_serialise_MultipleCaps() {
        val message = factory.serialise(CapNakMessage(caps = listOf("cap1", "cap2", "cap3")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("NAK", "cap1 cap2 cap3")), message)
    }

}