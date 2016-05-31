package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class PingMessageTests {
    lateinit var factory: PingMessage.Factory

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