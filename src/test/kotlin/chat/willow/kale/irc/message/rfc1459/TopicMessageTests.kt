package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class TopicMessageTests {
    lateinit var factory: TopicMessage.Factory

    @Before fun setUp() {
        factory = TopicMessage
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