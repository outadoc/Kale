package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class TopicMessageTests {
    lateinit var factory: IMessageFactory<TopicMessage>

    @Before fun setUp() {
        factory = TopicMessage.Factory
    }

    @Test fun test_parse_Channel() {
        val message = factory.parse(IrcMessage(command = "TOPIC", parameters = listOf("#channel")))

        assertEquals(TopicMessage(channel = "#channel"), message)
    }

    @Test fun test_parse_Source_Channel() {
        val message = factory.parse(IrcMessage(command = "TOPIC", prefix = "source", parameters = listOf("#channel")))

        assertEquals(TopicMessage(source = Prefix(nick = "source"), channel = "#channel"), message)
    }

    @Test fun test_parse_Source_Channel_Topic() {
        val message = factory.parse(IrcMessage(command = "TOPIC", prefix = "source", parameters = listOf("#channel", "channel topic!")))

        assertEquals(TopicMessage(source = Prefix(nick = "source"), channel = "#channel", topic = "channel topic!"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "TOPIC", parameters = listOf()))

        assertNull(message)
    }

    @Test fun test_serialise_Channel() {
        val message = factory.serialise(TopicMessage(channel = "#channel"))

        assertEquals(IrcMessage(command = "TOPIC", parameters = listOf("#channel")), message)
    }

    @Test fun test_serialise_Source_Channel() {
        val message = factory.serialise(TopicMessage(source = Prefix(nick = "someone"), channel = "#channel"))

        assertEquals(IrcMessage(command = "TOPIC", prefix = "someone", parameters = listOf("#channel")), message)
    }

    @Test fun test_serialise_Source_Channel_Topic() {
        val message = factory.serialise(TopicMessage(source = Prefix(nick = "someone"), channel = "#channel", topic = "another channel topic!"))

        assertEquals(IrcMessage(command = "TOPIC", prefix = "someone", parameters = listOf("#channel", "another channel topic!")), message)
    }

}