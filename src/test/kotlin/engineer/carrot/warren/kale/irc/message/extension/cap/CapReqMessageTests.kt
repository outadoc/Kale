package engineer.carrot.warren.kale.irc.message.extension.cap

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CapReqMessageTests {
    lateinit var factory: CapReqMessage.Factory

    @Before fun setUp() {
        factory = CapReqMessage
    }

    @Test fun test_parse_SingleCap() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "REQ", "cap1 ")))

        assertEquals(CapReqMessage(target = "test-nick", caps = listOf("cap1")), message)
    }

    @Test fun test_parse_MultipleCaps() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "REQ", "cap1 cap2 cap3")))

        assertEquals(CapReqMessage(target = "test-nick", caps = listOf("cap1", "cap2", "cap3")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "CAP", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick")))
        val messageThree = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "REQ")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_SingleCap() {
        val message = factory.serialise(CapReqMessage(caps = listOf("cap1")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("REQ", "cap1")), message)
    }

    @Test fun test_serialise_MultipleCaps() {
        val message = factory.serialise(CapReqMessage(caps = listOf("cap1", "cap2", "cap3")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("REQ", "cap1 cap2 cap3")), message)
    }

}