package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class Rpl332MessageTests {
    lateinit var factory: IMessageFactory<Rpl332Message>

    @Before fun setUp() {
        factory = Rpl332Message.Factory
    }

    @Test fun test_parse_SourceChannelContents() {
        val message = factory.parse(IrcMessage(command = "332", prefix = "imaginary.bunnies.io", parameters = listOf("#channel", "Channel topic!")))

        assertEquals(Rpl332Message(source = "imaginary.bunnies.io", channel = "#channel", topic = "Channel topic!"), message)
    }

    @Test fun test_parse_ChannelContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "332", parameters = listOf("#channel2", "Channel topic!")))

        assertEquals(Rpl332Message(source = "", channel = "#channel2", topic = "Channel topic!"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "332", parameters = listOf("#channel3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceChannelContents() {
        val message = factory.serialise(Rpl332Message(source = "", channel = "#channel2", topic = "Channel topic!"))

        assertEquals(IrcMessage(command = "332", prefix = "", parameters = listOf("#channel2", "Channel topic!")), message)
    }

    @Test fun test_serialise_ChannelContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl332Message(source = "", channel = "#channel2", topic = "Channel topic!"))

        assertEquals(IrcMessage(command = "332", prefix = "", parameters = listOf("#channel2", "Channel topic!")), message)
    }
}