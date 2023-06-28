package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.core.message.IrcMessage
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class PongMessageTests {

    private lateinit var messageParser: PongMessage.Message.Parser
    private lateinit var messageSerialiser: PongMessage.Message.Serialiser

    @BeforeTest fun setUp() {
        messageParser = PongMessage.Message.Parser
        messageSerialiser = PongMessage.Message.Serialiser
    }

    @Test fun test_parse() {
        val message = messageParser.parse(IrcMessage(command = "PONG", parameters = listOf("token1")))

        assertEquals(message, PongMessage.Message(token = "token1"))
    }

    @Test fun test_parse_noParameters() {
        val message = messageParser.parse(IrcMessage(command = "PONG"))

        assertNull(message)
    }

    @Test fun test_serialise() {
        val message = messageSerialiser.serialise(PongMessage.Message(token = "anotherToken"))

        assertEquals(message, IrcMessage(command = "PONG", parameters = listOf("anotherToken")))
    }
}