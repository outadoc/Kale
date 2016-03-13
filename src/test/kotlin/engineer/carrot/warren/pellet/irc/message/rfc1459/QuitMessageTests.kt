package engineer.carrot.warren.pellet.irc.message.rfc1459

import engineer.carrot.warren.pellet.irc.message.IMessageFactory
import engineer.carrot.warren.pellet.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class QuitMessageTests {
    lateinit var factory: IMessageFactory<QuitMessage>

    @Before fun setUp() {
        factory = QuitMessage.Factory
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

    @Test fun test_serialise_noMessage() {
        val message = factory.serialise(QuitMessage())

        assertEquals(message, IrcMessage(command = "QUIT"))
    }

    @Test fun test_serialise_withMessage() {
        val message = factory.serialise(QuitMessage(message = "A test quit message"))

        assertEquals(message, IrcMessage(command = "QUIT", parameters = listOf("A test quit message")))
    }
}