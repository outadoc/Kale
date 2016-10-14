package engineer.carrot.warren.kale.irc.message.extension.batch

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class BatchEndMessageTests {

    private lateinit var factory: BatchEndMessage.Factory

    @Before fun setUp() {
        factory = BatchEndMessage
    }

    @Test fun test_parse_ReferenceWithCorrectToken() {
        val message = factory.parse(IrcMessage(command = "BATCH", parameters = listOf("-batch1")))

        assertEquals(BatchEndMessage(reference = "batch1"), message)
    }

    @Test fun test_parse_MissingMinusCharacter_ReturnsNull() {
        val message = factory.parse(IrcMessage(command = "BATCH", parameters = listOf("batch1")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters_ReturnsNull() {
        val messageOne = factory.parse(IrcMessage(command = "BATCH", parameters = listOf()))

        assertNull(messageOne)
    }

    @Test fun test_serialise_WithReference() {
        val message = factory.serialise(BatchEndMessage(reference = "reference"))

        assertEquals(IrcMessage(command = "BATCH", parameters = listOf("-reference")), message)
    }

}