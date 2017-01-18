package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class PongMessageTests {
    lateinit var factory: PongMessage.Factory

    @Before fun setUp() {
        factory = PongMessage
    }

    @Test fun test_parse() {
        val message = factory.parse(IrcMessage(command = "PONG", parameters = listOf("token1")))

        assertEquals(message, PongMessage(token = "token1"))
    }

    @Test fun test_parse_noParameters() {
        val message = factory.parse(IrcMessage(command = "PONG"))

        assertNull(message)
    }

    @Test fun test_serialise() {
        val message = factory.serialise(PongMessage(token = "anotherToken"))

        assertEquals(message, IrcMessage(command = "PONG", parameters = listOf("anotherToken")))
    }
}