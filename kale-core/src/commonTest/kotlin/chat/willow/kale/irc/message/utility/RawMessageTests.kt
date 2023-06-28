package chat.willow.kale.irc.message.utility

import chat.willow.kale.core.message.IrcMessage
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class RawMessageTests {

    private lateinit var messageSerialiser: RawMessage.Line.Serialiser

    @BeforeTest fun setUp() {
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