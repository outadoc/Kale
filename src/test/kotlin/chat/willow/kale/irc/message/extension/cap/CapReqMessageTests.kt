package chat.willow.kale.irc.message.extension.cap

import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CapReqMessageTests {

    lateinit var messageParser: CapMessage.Req.Command.Parser
    lateinit var messageSerialiser: CapMessage.Req.Command.Serialiser

    @Before fun setUp() {
        messageParser = CapMessage.Req.Command.Parser
        messageSerialiser = CapMessage.Req.Command.Serialiser
    }

    @Test fun test_parse_SingleCap() {
        val message = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("REQ", "cap1 ")))

        assertEquals(CapMessage.Req.Command(caps = listOf("cap1")), message)
    }

    @Test fun test_parse_MultipleCaps() {
        val message = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("REQ", "cap1 cap2 cap3")))

        assertEquals(CapMessage.Req.Command(caps = listOf("cap1", "cap2", "cap3")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf()))
        val messageTwo = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick")))
        val messageThree = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "REQ")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_SingleCap() {
        val message = messageSerialiser.serialise(CapMessage.Req.Command(caps = listOf("cap1")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("REQ", "cap1")), message)
    }

    @Test fun test_serialise_MultipleCaps() {
        val message = messageSerialiser.serialise(CapMessage.Req.Command(caps = listOf("cap1", "cap2", "cap3")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("REQ", "cap1 cap2 cap3")), message)
    }

}