package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class QuitMessageTests {
    lateinit var factory: QuitMessage.Factory

    @Before fun setUp() {
        factory = QuitMessage
    }

    @Test fun test_parse_noParameters() {
        val message = factory.parse(IrcMessage(command = "QUIT"))

        assertEquals(message, QuitMessage())
    }

    @Test fun test_parse_withMessage() {
        val message = factory.parse(IrcMessage(command = "QUIT", parameters = listOf("a test quit message")))

        assertEquals(message, QuitMessage(message = "a test quit message"))
    }

    @Test fun test_parse_withEmptyMessage() {
        val message = factory.parse(IrcMessage(command = "QUIT", parameters = listOf("")))

        assertEquals(message, QuitMessage(message = ""))
    }

    @Test fun test_parse_tooManyParameters() {
        val message = factory.parse(IrcMessage(command = "QUIT", parameters = listOf("1", "2", "3")))

        assertEquals(message, QuitMessage(message = "1"))
    }

    @Test fun test_parse_WithSource() {
        val message = factory.parse(IrcMessage(command = "QUIT", prefix = "someone@somewhere"))

        assertEquals(QuitMessage(source = Prefix(nick = "someone", host = "somewhere")), message)
    }

    @Test fun test_serialise_noMessage() {
        val message = factory.serialise(QuitMessage())

        assertEquals(message, IrcMessage(command = "QUIT"))
    }

    @Test fun test_serialise_withMessage() {
        val message = factory.serialise(QuitMessage(message = "A test quit message"))

        assertEquals(message, IrcMessage(command = "QUIT", parameters = listOf("A test quit message")))
    }

    @Test fun test_serialise_WithSource() {
        val message = factory.serialise(QuitMessage(source = Prefix(nick = "someone", host = "somewhere")))

        assertEquals(IrcMessage(command = "QUIT", prefix = "someone@somewhere"), message)
    }
}