package chat.willow.kale.irc.message.extension.sasl

import chat.willow.kale.core.message.IrcMessage
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class AuthenticateMessageTests {

    private lateinit var messageParser: AuthenticateMessage.Message.Parser
    private lateinit var messageSerialiser: AuthenticateMessage.Message.Serialiser

    @BeforeTest fun setUp() {
        messageParser = AuthenticateMessage.Message.Parser
        messageSerialiser = AuthenticateMessage.Message.Serialiser
    }

    @Test fun test_parse_NotEmpty() {
        val message = messageParser.parse(IrcMessage(command = "AUTHENTICATE", parameters = listOf("base64payload")))

        assertEquals(message, AuthenticateMessage.Message(payload = "base64payload", isEmpty = false))
    }

    @Test fun test_parse_Empty() {
        val message = messageParser.parse(IrcMessage(command = "AUTHENTICATE", parameters = listOf("+")))

        assertEquals(message, AuthenticateMessage.Message(payload = "+", isEmpty = true))
    }

    @Test fun test_parse_noParameters() {
        val message = messageParser.parse(IrcMessage(command = "AUTHENTICATE"))

        assertNull(message)
    }

    @Test fun test_serialise_NotEmpty() {
        val message = messageSerialiser.serialise(AuthenticateMessage.Message(payload = "base64payload", isEmpty = false))

        assertEquals(message, IrcMessage(command = "AUTHENTICATE", parameters = listOf("base64payload")))
    }

    @Test fun test_serialise_Empty() {
        val message = messageSerialiser.serialise(AuthenticateMessage.Message(payload = "anotherpayload", isEmpty = true))

        assertEquals(message, IrcMessage(command = "AUTHENTICATE", parameters = listOf("+")))
    }

    // TODO: test Command too
}