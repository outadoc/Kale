package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class Rpl331MessageTests {
    lateinit var factory: IMessageFactory<Rpl331Message>

    @Before fun setUp() {
        factory = Rpl331Message.Factory
    }

    @Test fun test_parse_SourceChannelContents() {
        val message = factory.parse(IrcMessage(command = "331", prefix = "imaginary.bunnies.io", parameters = listOf("#channel", "No topic is set")))

        assertEquals(Rpl331Message(source = "imaginary.bunnies.io", channel = "#channel", contents = "No topic is set"), message)
    }

    @Test fun test_parse_ChannelContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "331", parameters = listOf("#channel2", "No topic is set")))

        assertEquals(Rpl331Message(source = "", channel = "#channel2", contents = "No topic is set"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "331", parameters = listOf("#channel3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceChannelContents() {
        val message = factory.serialise(Rpl331Message(source = "", channel = "#channel2", contents = "No topic is set"))

        assertEquals(IrcMessage(command = "331", prefix = "", parameters = listOf("#channel2", "No topic is set")), message)
    }

    @Test fun test_serialise_ChannelContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl331Message(source = "", channel = "#channel2", contents = "No topic is set"))

        assertEquals(IrcMessage(command = "331", prefix = "", parameters = listOf("#channel2", "No topic is set")), message)
    }
}