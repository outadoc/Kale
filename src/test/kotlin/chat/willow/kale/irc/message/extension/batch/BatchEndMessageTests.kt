package chat.willow.kale.irc.message.extension.batch

import chat.willow.kale.core.message.IrcMessage
import chat.willow.kale.irc.prefix.prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class BatchEndMessageTests {

    private lateinit var messageParser: BatchMessage.End.Message.Parser
    private lateinit var messageSerialiser: BatchMessage.End.Message.Serialiser

    @Before fun setUp() {
        messageParser = BatchMessage.End.Message.Parser
        messageSerialiser= BatchMessage.End.Message.Serialiser
    }

    @Test fun test_parse_ReferenceWithCorrectToken() {
        val message = messageParser.parse(IrcMessage(command = "BATCH", prefix = "someone", parameters = listOf("-batch1")))

        assertEquals(BatchMessage.End.Message(source = prefix("someone"), reference = "batch1"), message)
    }

    @Test fun test_parse_MissingMinusCharacter_ReturnsNull() {
        val message = messageParser.parse(IrcMessage(command = "BATCH", prefix = "someone", parameters = listOf("batch1")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters_ReturnsNull() {
        val messageOne = messageParser.parse(IrcMessage(command = "BATCH", prefix = "someone", parameters = listOf()))

        assertNull(messageOne)
    }

    @Test fun test_parse_NoPrefix_ReturnsNull() {
        val messageOne = messageParser.parse(IrcMessage(command = "BATCH", prefix = null, parameters = listOf("-batch1")))

        assertNull(messageOne)
    }

    @Test fun test_serialise_WithReference() {
        val message = messageSerialiser.serialise(BatchMessage.End.Message(source = prefix("someone"), reference = "reference"))

        assertEquals(IrcMessage(command = "BATCH", prefix = "someone", parameters = listOf("-reference")), message)
    }

}