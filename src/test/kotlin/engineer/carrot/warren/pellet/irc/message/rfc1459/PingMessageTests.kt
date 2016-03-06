package engineer.carrot.warren.pellet.irc.message.rfc1459

import engineer.carrot.warren.pellet.irc.message.IMessageFactory
import engineer.carrot.warren.pellet.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class PingMessageTests {
    lateinit var factory: IMessageFactory<PingMessage>

    @Before fun setUp() {
        factory = PingMessage.Factory
    }

    @Test fun test_parse() {
        val message = factory.parse(IrcMessage(command = "PING", parameters = listOf("token1")))

        assertEquals(message, PingMessage(token = "token1"))
    }

    @Test fun test_parse_noParameters() {
        val message = factory.parse(IrcMessage(command = "PING"))

        assertNull(message)
    }

    @Test fun test_serialise() {
        val message = factory.serialise(PingMessage(token = "anotherToken"))

        assertEquals(message, IrcMessage(command = "PING", parameters = listOf("anotherToken")))
    }
}