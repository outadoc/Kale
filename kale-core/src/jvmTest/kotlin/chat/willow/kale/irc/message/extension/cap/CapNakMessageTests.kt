package chat.willow.kale.irc.message.extension.cap

import chat.willow.kale.core.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CapNakMessageTests {

    lateinit var messageParser: CapMessage.Nak.Message.Parser
    lateinit var messageSerialiser: CapMessage.Nak.Message.Serialiser

    @Before fun setUp() {
        messageParser = CapMessage.Nak.Message.Parser
        messageSerialiser = CapMessage.Nak.Message.Serialiser
    }

    @Test fun test_parse_SingleCap() {
        val message = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NAK", "cap1 ")))

        assertEquals(CapMessage.Nak.Message(source = null, target = "test-nick", caps = listOf("cap1")), message)
    }

    @Test fun test_parse_MultipleCaps() {
        val message = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NAK", "cap1 cap2 cap3")))

        assertEquals(CapMessage.Nak.Message(source = null, target = "test-nick", caps = listOf("cap1", "cap2", "cap3")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf()))
        val messageTwo = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick")))
        val messageThree = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NAK")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_SingleCap() {
        val message = messageSerialiser.serialise(CapMessage.Nak.Message(source = null, target = "someone", caps = listOf("cap1")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("someone", "NAK", "cap1")), message)
    }

    @Test fun test_serialise_MultipleCaps() {
        val message = messageSerialiser.serialise(CapMessage.Nak.Message(source = null, target = "someone", caps = listOf("cap1", "cap2", "cap3")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("someone", "NAK", "cap1 cap2 cap3")), message)
    }

}