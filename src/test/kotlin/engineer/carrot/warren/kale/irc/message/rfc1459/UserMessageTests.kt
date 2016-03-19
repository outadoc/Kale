package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class UserMessageTests {
    lateinit var factory: IMessageFactory<UserMessage>

    @Before fun setUp() {
        factory = UserMessage.Factory
    }

    @Test fun test_parse() {
        val message = factory.parse(IrcMessage(command = "USER", parameters = listOf("1", "2", "3", "4")))

        assertEquals(message, UserMessage(username = "1", hostname = "2", servername = "3", realname = "4"))
    }

    @Test fun test_parse_tooFewParameters() {
        val messageZero = factory.parse(IrcMessage(command = "USER"))
        val messageOne = factory.parse(IrcMessage(command = "USER", parameters = listOf("1")))
        val messageTwo = factory.parse(IrcMessage(command = "USER", parameters = listOf("1", "2")))
        val messageThree = factory.parse(IrcMessage(command = "USER", parameters = listOf("1", "2", "3")))

        assertNull(messageZero)
        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise() {
        val message = factory.serialise(UserMessage(username = "username", hostname = "hostname", servername = "servername", realname = "realname"))

        assertEquals(message, IrcMessage(command = "USER", parameters = listOf("username", "hostname", "servername", "realname")))
    }
}