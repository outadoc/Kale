package chat.willow.kale.irc.message.extension.cap

import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CapEndMessageTests {

    lateinit var messageParser: CapMessage.End.Command.Parser
    lateinit var messageSerialiser: CapMessage.End.Command.Serialiser

    @Before fun setUp() {
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