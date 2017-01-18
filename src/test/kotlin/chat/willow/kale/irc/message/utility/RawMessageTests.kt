package chat.willow.kale.irc.message.utility

import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RawMessageTests {
    lateinit var factory: RawMessage.Factory

    @Before fun setUp() {
        factory = RawMessage
    }

    @Test fun test_serialise_WellFormedLine() {
        val message = factory.serialise(RawMessage(line = ":prefix 123 1 :2 3"))

        assertEquals(IrcMessage(command = "123", prefix = "prefix", parameters = listOf("1", "2 3")), message)
    }

    @Test fun test_serialise_BadlyFormedLine_Empty() {
        val message = factory.serialise(RawMessage(line = ""))

        assertNull(message)
    }

    @Test fun test_serialise_BadlyFormedLine_Garbage() {
        val message = factory.serialise(RawMessage(line = ": :1 :2 :3"))

        assertNull(message)
    }

}