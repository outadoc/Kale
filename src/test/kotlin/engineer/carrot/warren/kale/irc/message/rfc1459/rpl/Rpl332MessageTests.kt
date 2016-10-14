package engineer.carrot.warren.kale.irc.message.rfc1459.rpl

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl332MessageTests {
    lateinit var factory: Rpl332Message.Factory

    @Before fun setUp() {
        factory = Rpl332Message.Factory
    }

    @Test fun test_parse_SourceChannelContents() {
        val message = factory.parse(IrcMessage(command = "332", prefix = "imaginary.bunnies.io", parameters = listOf("test-user", "#channel", "Channel topic!")))

        assertEquals(Rpl332Message(source = "imaginary.bunnies.io", target = "test-user", channel = "#channel", topic = "Channel topic!"), message)
    }

    @Test fun test_parse_ChannelContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "332", parameters = listOf("test-user", "#channel2", "Channel topic!")))

        assertEquals(Rpl332Message(source = "", target = "test-user", channel = "#channel2", topic = "Channel topic!"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "332", parameters = listOf("test-user")))
        val messageTwo = factory.parse(IrcMessage(command = "332", parameters = listOf("test-user", "#channel")))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_SourceChannelContents() {
        val message = factory.serialise(Rpl332Message(source = "", target = "test-user", channel = "#channel2", topic = "Channel topic!"))

        assertEquals(IrcMessage(command = "332", prefix = "", parameters = listOf("test-user", "#channel2", "Channel topic!")), message)
    }

    @Test fun test_serialise_ChannelContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl332Message(source = "", target = "test-user", channel = "#channel2", topic = "Channel topic!"))

        assertEquals(IrcMessage(command = "332", prefix = "", parameters = listOf("test-user", "#channel2", "Channel topic!")), message)
    }
}