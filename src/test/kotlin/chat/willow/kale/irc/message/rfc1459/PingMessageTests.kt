package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.generator.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class PingMessageTests {

    private lateinit var messageParser: PingMessage.Command.Parser
    private lateinit var messageSerialiser: PingMessage.Command.Serialiser

    @Before fun setUp() {
        messageParser = PingMessage.Command.Parser
        messageSerialiser = PingMessage.Command.Serialiser
    }

    @Test fun test_parse() {
        val message = messageParser.parse(IrcMessage(command = "PING", parameters = listOf("token1")))

        assertEquals(message, PingMessage.Command(token = "token1"))
    }

    @Test fun test_parse_noParameters() {
        val message = messageParser.parse(IrcMessage(command = "PING"))

        assertNull(message)
    }

    @Test fun test_serialise() {
        val message = messageSerialiser.serialise(PingMessage.Command(token = "anotherToken"))

        assertEquals(message, IrcMessage(command = "PING", parameters = listOf("anotherToken")))
    }
}