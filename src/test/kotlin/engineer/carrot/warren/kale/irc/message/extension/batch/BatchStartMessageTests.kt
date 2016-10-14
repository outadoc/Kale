package engineer.carrot.warren.kale.irc.message.extension.batch

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class BatchStartMessageTests {

    private lateinit var factory: BatchStartMessage.Factory

    @Before fun setUp() {
        factory = BatchStartMessage
    }

    @Test fun test_parse_NoBatchParameters() {
        val message = factory.parse(IrcMessage(command = "BATCH", parameters = listOf("+batch1", "type")))

        assertEquals(BatchStartMessage(reference = "batch1", type = "type"), message)
    }

    @Test fun test_parse_MultipleBatchParameters() {
        val message = factory.parse(IrcMessage(command = "BATCH", parameters = listOf("+batch1", "type", "param1", "param2")))

        assertEquals(BatchStartMessage(reference = "batch1", type = "type", parameters = listOf("param1", "param2")), message)
    }

    @Test fun test_parse_MissingPlusCharacter_ReturnsNull() {
        val message = factory.parse(IrcMessage(command = "BATCH", parameters = listOf("batch1", "type")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters_ReturnsNull() {
        val messageOne = factory.parse(IrcMessage(command = "BATCH", parameters = listOf("batch1")))
        val messageTwo = factory.parse(IrcMessage(command = "BATCH", parameters = listOf()))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_NoBatchParameters() {
        val message = factory.serialise(BatchStartMessage(reference = "reference", type = "type"))

        assertEquals(IrcMessage(command = "BATCH", parameters = listOf("+reference", "type")), message)
    }

    @Test fun test_serialise_MultipleBatchParameters() {
        val message = factory.serialise(BatchStartMessage(reference = "reference", type = "type", parameters = listOf("parameter1", "parameter2")))

        assertEquals(IrcMessage(command = "BATCH", parameters = listOf("+reference", "type", "parameter1", "parameter2")), message)
    }

}