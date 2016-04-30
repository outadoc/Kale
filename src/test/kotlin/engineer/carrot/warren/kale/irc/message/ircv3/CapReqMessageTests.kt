package engineer.carrot.warren.kale.irc.message.ircv3

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class CapReqMessageTests {
    lateinit var factory: IMessageFactory<CapReqMessage>

    @Before fun setUp() {
        factory = CapReqMessage.Factory
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