package chat.willow.kale.irc.message.utility

import chat.willow.kale.generator.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RawMessageTests {

    private lateinit var messageSerialiser: RawMessage.Line.Serialiser

    @Before fun setUp() {
        messageSerialiser = RawMessage.Line.Serialiser
    }

    @Test fun test_serialise_WellFormedLine() {
        val message = messageSerialiser.serialise(RawMessage.Line(line = ":prefix 123 1 :2 3"))

        assertEquals(IrcMessage(command = "123", prefix = "prefix", parameters = listOf("1", "2 3")), message)
    }

    @Test fun test_serialise_BadlyFormedLine_Empty() {
        val message = messageSerialiser.serialise(RawMessage.Line(line = ""))

        assertNull(message)
    }

    @Test fun test_serialise_BadlyFormedLine_Garbage() {
        val message = messageSerialiser.serialise(RawMessage.Line(line = ": :1 :2 :3"))

        assertNull(message)
    }

}