package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class UserMessageTests {
    lateinit var factory: UserMessage.Factory

    @Before fun setUp() {
        factory = UserMessage
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