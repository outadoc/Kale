package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.core.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class PassMessageTests {

    private lateinit var messageParser: PassMessage.Command.Parser
    private lateinit var messageSerialiser: PassMessage.Command.Serialiser

    @Before fun setUp() {
        messageParser = PassMessage.Command.Parser
        messageSerialiser = PassMessage.Command.Serialiser
    }

    @Test fun test_parse() {
        val message = messageParser.parse(IrcMessage(command = "PASS", parameters = listOf("password")))

        assertEquals(message, PassMessage.Command(password = "password"))
    }

    @Test fun test_parse_tooFewParameters() {
        val message = messageParser.parse(IrcMessage(command = "PASS", parameters = listOf()))

        assertNull(message)
    }

    @Test fun test_serialise() {
        val message = messageSerialiser.serialise(PassMessage.Command(password = "password"))

        assertEquals(message, IrcMessage(command = "PASS", parameters = listOf("password")))
    }

}