package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.core.message.IrcMessage
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class UserMessageTests {

    private lateinit var messageParser: UserMessage.Command.Parser
    private lateinit var messageSerialiser: UserMessage.Command.Serialiser

    @BeforeTest fun setUp() {
        messageParser = UserMessage.Command.Parser
        messageSerialiser = UserMessage.Command.Serialiser
    }

    @Test fun test_parse() {
        val message = messageParser.parse(IrcMessage(command = "USER", parameters = listOf("username", "mode", "unused", "realname")))

        assertEquals(message, UserMessage.Command(username = "username", mode = "mode", realname = "realname"))
    }

    @Test fun test_parse_tooFewParameters() {
        val messageZero = messageParser.parse(IrcMessage(command = "USER"))
        val messageOne = messageParser.parse(IrcMessage(command = "USER", parameters = listOf("1")))
        val messageTwo = messageParser.parse(IrcMessage(command = "USER", parameters = listOf("1", "2")))
        val messageThree = messageParser.parse(IrcMessage(command = "USER", parameters = listOf("1", "2", "3")))

        assertNull(messageZero)
        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise() {
        val message = messageSerialiser.serialise(UserMessage.Command(username = "username", mode = "mode", realname = "realname"))

        assertEquals(message, IrcMessage(command = "USER", parameters = listOf("username", "mode", "*", "realname")))
    }
}