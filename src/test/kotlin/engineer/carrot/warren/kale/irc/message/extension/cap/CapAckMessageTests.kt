package engineer.carrot.warren.kale.irc.message.extension.cap

import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.message.extension.cap.CapAckMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CapAckMessageTests {
    lateinit var factory: CapAckMessage.Factory

    @Before fun setUp() {
        factory = CapAckMessage
    }

    @Test fun test_parse_SingleCap() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "ACK", "cap1 ")))

        assertEquals(CapAckMessage(target = "test-nick", caps = listOf("cap1")), message)
    }

    @Test fun test_parse_MultipleCaps() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "ACK", "cap1 cap2 cap3")))

        assertEquals(CapAckMessage(target = "test-nick", caps = listOf("cap1", "cap2", "cap3")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "CAP", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick")))
        val messageThree = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "ACK")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_SingleCap() {
        val message = factory.serialise(CapAckMessage(caps = listOf("cap1")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("ACK", "cap1")), message)
    }

    @Test fun test_serialise_MultipleCaps() {
        val message = factory.serialise(CapAckMessage(caps = listOf("cap1", "cap2", "cap3")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("ACK", "cap1 cap2 cap3")), message)
    }

}