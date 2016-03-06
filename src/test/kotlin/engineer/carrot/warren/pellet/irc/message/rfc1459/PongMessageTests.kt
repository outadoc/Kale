package engineer.carrot.warren.pellet.irc.message.rfc1459

import engineer.carrot.warren.pellet.irc.message.IMessageFactory
import engineer.carrot.warren.pellet.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class PongMessageTests {
    lateinit var factory: IMessageFactory<PongMessage>

    @Before fun setUp() {
        factory = PongMessage.Factory
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