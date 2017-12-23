package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.generator.message.IrcMessage
import chat.willow.kale.irc.prefix.prefix
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class QuitMessageTests {

    private lateinit var messageParser: QuitMessage.Message.Parser
    private lateinit var messageSerialiser: QuitMessage.Command.Serialiser

    @Before fun setUp() {
        messageParser = QuitMessage.Message.Parser
        messageSerialiser = QuitMessage.Command.Serialiser
    }

    @Test fun test_parse_noParameters() {
        val message = messageParser.parse(IrcMessage(command = "QUIT", prefix = "someone"))

        assertEquals(message, QuitMessage.Message(source = prefix("someone")))
    }

    @Test fun test_parse_withMessage() {
        val message = messageParser.parse(IrcMessage(command = "QUIT", prefix = "someone", parameters = listOf("a test quit message")))

        assertEquals(message, QuitMessage.Message(source = prefix("someone"), message = "a test quit message"))
    }

    @Test fun test_parse_withEmptyMessage() {
        val message = messageParser.parse(IrcMessage(command = "QUIT", prefix = "someone", parameters = listOf("")))

        assertEquals(message, QuitMessage.Message(source = prefix("someone"), message = ""))
    }

    @Test fun test_parse_tooManyParameters() {
        val message = messageParser.parse(IrcMessage(command = "QUIT", prefix = "someone", parameters = listOf("1", "2", "3")))

        assertEquals(message, QuitMessage.Message(source = prefix("someone"), message = "1"))
    }

    @Test fun test_serialise_noMessage() {
        val message = messageSerialiser.serialise(QuitMessage.Command())

        assertEquals(message, IrcMessage(command = "QUIT"))
    }

    @Test fun test_serialise_withMessage() {
        val message = messageSerialiser.serialise(QuitMessage.Command(message = "A test quit message"))

        assertEquals(message, IrcMessage(command = "QUIT", parameters = listOf("A test quit message")))
    }

}