package engineer.carrot.warren.kale.irc.message.rfc1459.rpl

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl353MessageTests {
    lateinit var factory: Rpl353Message.Factory

    @Before fun setUp() {
        factory = Rpl353Message.Factory
    }

    @Test fun test_parse_SingleUser_NoPrefixes() {
        val parameters = listOf("test-user", "@", "#channel", "test-user")
        val message = factory.parse(IrcMessage(command = "353", prefix = "imaginary.bunnies.io", parameters = parameters))

        assertEquals(Rpl353Message(source = "imaginary.bunnies.io", target = "test-user", visibility = "@", channel = "#channel", names = listOf("test-user")), message)
    }

    @Test fun test_parse_MultipleUsers_DifferingPrefixes() {
        val parameters = listOf("test-user", "=", "#channel", "@test-user another-user")
        val message = factory.parse(IrcMessage(command = "353", prefix = "imaginary.bunnies.io", parameters = parameters))

        assertEquals(Rpl353Message(source = "imaginary.bunnies.io", target = "test-user", visibility = "=", channel = "#channel", names = listOf("@test-user", "another-user")), message)
    }

    @Test fun test_parse_MultipleUsers_DifferingPrefixes_TrailingWhitespace() {
        val parameters = listOf("test-user", "*", "#channel", "@test-user another-user ")
        val message = factory.parse(IrcMessage(command = "353", prefix = "imaginary.bunnies.io", parameters = parameters))

        assertEquals(Rpl353Message(source = "imaginary.bunnies.io", target = "test-user", visibility = "*", channel = "#channel", names = listOf("@test-user", "another-user")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "353", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "353", parameters = listOf("test-user")))
        val messageThree = factory.parse(IrcMessage(command = "353", parameters = listOf("test-user", "@")))
        val messageFour = factory.parse(IrcMessage(command = "353", parameters = listOf("test-user", "@", "#channel")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
        assertNull(messageFour)
    }

    @Test fun test_serialise_SingleUser_NoPrefixes() {
        val message = factory.serialise(Rpl353Message(source = "imaginary.bunnies.io", target = "test-user", visibility = "@", channel = "#channel", names = listOf("test-user")))

        assertEquals(IrcMessage(command = "353", prefix = "imaginary.bunnies.io", parameters = listOf("test-user", "@", "#channel", "test-user")), message)
    }

    @Test fun test_serialise_MultipleUsers_DifferingPrefixes() {
        val message = factory.serialise(Rpl353Message(source = "imaginary.bunnies.io", target = "test-user", visibility = "*", channel = "#channel", names = listOf("test-user", "@another-user")))

        assertEquals(IrcMessage(command = "353", prefix = "imaginary.bunnies.io", parameters = listOf("test-user", "*", "#channel", "test-user @another-user")), message)
    }
}