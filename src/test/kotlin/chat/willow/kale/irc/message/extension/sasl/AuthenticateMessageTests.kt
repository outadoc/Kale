package chat.willow.kale.irc.message.extension.sasl

import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AuthenticateMessageTests {
    lateinit var factory: AuthenticateMessage.Factory

    @Before fun setUp() {
        factory = AuthenticateMessage
    }

    @Test fun test_parse_NotEmpty() {
        val message = factory.parse(IrcMessage(command = "AUTHENTICATE", parameters = listOf("base64payload")))

        assertEquals(message, AuthenticateMessage(payload = "base64payload", isEmpty = false))
    }

    @Test fun test_parse_Empty() {
        val message = factory.parse(IrcMessage(command = "AUTHENTICATE", parameters = listOf("+")))

        assertEquals(message, AuthenticateMessage(payload = "+", isEmpty = true))
    }

    @Test fun test_parse_noParameters() {
        val message = factory.parse(IrcMessage(command = "AUTHENTICATE"))

        assertNull(message)
    }

    @Test fun test_serialise_NotEmpty() {
        val message = factory.serialise(AuthenticateMessage(payload = "base64payload", isEmpty = false))

        assertEquals(message, IrcMessage(command = "AUTHENTICATE", parameters = listOf("base64payload")))
    }

    @Test fun test_serialise_Empty() {
        val message = factory.serialise(AuthenticateMessage(payload = "anotherpayload", isEmpty = true))

        assertEquals(message, IrcMessage(command = "AUTHENTICATE", parameters = listOf("+")))
    }
}