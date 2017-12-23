package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.generator.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl353MessageTests {

    private lateinit var messageParser: Rpl353Message.Message.Parser
    private lateinit var messageSerialiser: Rpl353Message.Message.Serialiser

    @Before fun setUp() {
        messageParser = Rpl353Message.Message.Parser
        messageSerialiser = Rpl353Message.Message.Serialiser
    }

    @Test fun test_parse_SingleUser_NoPrefixes() {
        val parameters = listOf("test-user", "@", "#channel", "test-user")
        val message = messageParser.parse(IrcMessage(command = "353", prefix = "imaginary.bunnies.io", parameters = parameters))

        assertEquals(Rpl353Message.Message(source = "imaginary.bunnies.io", target = "test-user", visibility = "@", channel = "#channel", names = listOf("test-user")), message)
    }

    @Test fun test_parse_MultipleUsers_DifferingPrefixes() {
        val parameters = listOf("test-user", "=", "#channel", "@test-user another-user")
        val message = messageParser.parse(IrcMessage(command = "353", prefix = "imaginary.bunnies.io", parameters = parameters))

        assertEquals(Rpl353Message.Message(source = "imaginary.bunnies.io", target = "test-user", visibility = "=", channel = "#channel", names = listOf("@test-user", "another-user")), message)
    }

    @Test fun test_parse_MultipleUsers_DifferingPrefixes_TrailingWhitespace() {
        val parameters = listOf("test-user", "*", "#channel", "@test-user another-user ")
        val message = messageParser.parse(IrcMessage(command = "353", prefix = "imaginary.bunnies.io", parameters = parameters))

        assertEquals(Rpl353Message.Message(source = "imaginary.bunnies.io", target = "test-user", visibility = "*", channel = "#channel", names = listOf("@test-user", "another-user")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "353", parameters = listOf()))
        val messageTwo = messageParser.parse(IrcMessage(command = "353", parameters = listOf("test-user")))
        val messageThree = messageParser.parse(IrcMessage(command = "353", parameters = listOf("test-user", "@")))
        val messageFour = messageParser.parse(IrcMessage(command = "353", parameters = listOf("test-user", "@", "#channel")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
        assertNull(messageFour)
    }

    @Test fun test_serialise_SingleUser_NoPrefixes() {
        val message = messageSerialiser.serialise(Rpl353Message.Message(source = "imaginary.bunnies.io", target = "test-user", visibility = "@", channel = "#channel", names = listOf("test-user")))

        assertEquals(IrcMessage(command = "353", prefix = "imaginary.bunnies.io", parameters = listOf("test-user", "@", "#channel", "test-user")), message)
    }

    @Test fun test_serialise_MultipleUsers_DifferingPrefixes() {
        val message = messageSerialiser.serialise(Rpl353Message.Message(source = "imaginary.bunnies.io", target = "test-user", visibility = "*", channel = "#channel", names = listOf("test-user", "@another-user")))

        assertEquals(IrcMessage(command = "353", prefix = "imaginary.bunnies.io", parameters = listOf("test-user", "*", "#channel", "test-user @another-user")), message)
    }
}