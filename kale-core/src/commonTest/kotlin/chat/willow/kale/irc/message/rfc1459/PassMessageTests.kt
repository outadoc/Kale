package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.core.message.IrcMessage
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class PassMessageTests {

    private lateinit var messageParser: PassMessage.Command.Parser
    private lateinit var messageSerialiser: PassMessage.Command.Serialiser

    @BeforeTest fun setUp() {
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