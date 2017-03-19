package chat.willow.kale.irc.message.extension.cap

import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CapNewMessageTests {

    lateinit var messageParser: CapMessage.New.Message.Parser
    lateinit var messageSerialiser: CapMessage.New.Message.Serialiser

    @Before fun setUp() {
        messageParser = CapMessage.New.Message.Parser
        messageSerialiser = CapMessage.New.Message.Serialiser
    }

    @Test fun test_parse_SingleCap() {
        val message = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NEW", "cap1 ")))

        assertEquals(CapMessage.New.Message(target = "test-nick", caps = mapOf("cap1" to null)), message)
    }

    @Test fun test_parse_MultipleCaps() {
        val message = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NEW", "cap1 cap2=value cap3=")))

        assertEquals(CapMessage.New.Message(target = "test-nick", caps = mapOf("cap1" to null, "cap2" to "value", "cap3" to "")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf()))
        val messageTwo = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick")))
        val messageThree = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "NEW")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_SingleCap() {
        val message = messageSerialiser.serialise(CapMessage.New.Message(target = "someone", caps = mapOf("cap1" to null)))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("someone", "NEW", "cap1")), message)
    }

    @Test fun test_serialise_MultipleCaps() {
        val message = messageSerialiser.serialise(CapMessage.New.Message(target = "someone", caps = mapOf("cap1" to null, "cap2" to "", "cap3" to "val3")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("someone", "NEW", "cap1 cap2= cap3=val3")), message)
    }

}