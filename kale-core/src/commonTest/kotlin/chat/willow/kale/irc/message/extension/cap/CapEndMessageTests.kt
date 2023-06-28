package chat.willow.kale.irc.message.extension.cap

import chat.willow.kale.core.message.IrcMessage
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class CapEndMessageTests {

    lateinit var messageParser: CapMessage.End.Command.Parser
    lateinit var messageSerialiser: CapMessage.End.Command.Serialiser

    @BeforeTest fun setUp() {
        messageParser = CapMessage.End.Command.Parser
        messageSerialiser = CapMessage.End.Command.Serialiser
    }

    @Test fun test_parse() {
        val message = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf("END")))

        assertEquals(CapMessage.End.Command, message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "CAP", parameters = listOf()))

        assertNull(messageOne)
    }

    @Test fun test_serialise() {
        val message = messageSerialiser.serialise(CapMessage.End.Command)

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("END")), message)
    }

}