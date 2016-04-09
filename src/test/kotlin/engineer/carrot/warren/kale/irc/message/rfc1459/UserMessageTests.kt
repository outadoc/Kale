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
        val message = factory.parse(IrcMessage(command = "USER", parameters = listOf("username", "mode", "unused", "realname")))

        assertEquals(message, UserMessage(username = "username", mode = "mode", realname = "realname"))
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
        val message = factory.serialise(UserMessage(username = "username", mode = "mode", realname = "realname"))

        assertEquals(message, IrcMessage(command = "USER", parameters = listOf("username", "mode", "*", "realname")))
    }
}