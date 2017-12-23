package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.core.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class TopicMessageTests {

    private lateinit var messageParser: TopicMessage.Message.Parser
    private lateinit var messageSerialiser: TopicMessage.Command.Serialiser

    @Before fun setUp() {
        messageParser = TopicMessage.Message.Parser
        messageSerialiser = TopicMessage.Command.Serialiser
    }

    @Test fun test_parse_Channel() {
        val message = messageParser.parse(IrcMessage(command = "TOPIC", prefix = "source", parameters = listOf("#channel")))

        assertEquals(TopicMessage.Message(source = Prefix(nick = "source"), channel = "#channel"), message)
    }

    @Test fun test_parse_Source_Channel() {
        val message = messageParser.parse(IrcMessage(command = "TOPIC", prefix = "source", parameters = listOf("#channel")))

        assertEquals(TopicMessage.Message(source = Prefix(nick = "source"), channel = "#channel"), message)
    }

    @Test fun test_parse_Source_Channel_Topic() {
        val message = messageParser.parse(IrcMessage(command = "TOPIC", prefix = "source", parameters = listOf("#channel", "channel topic!")))

        assertEquals(TopicMessage.Message(source = Prefix(nick = "source"), channel = "#channel", topic = "channel topic!"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = messageParser.parse(IrcMessage(command = "TOPIC", parameters = listOf()))

        assertNull(message)
    }

    @Test fun test_serialise_Source_Channel() {
        val message = messageSerialiser.serialise(TopicMessage.Command(channel = "#channel"))

        assertEquals(IrcMessage(command = "TOPIC", parameters = listOf("#channel")), message)
    }

    @Test fun test_serialise_Source_Channel_Topic() {
        val message = messageSerialiser.serialise(TopicMessage.Command(channel = "#channel", topic = "another channel topic!"))

        assertEquals(IrcMessage(command = "TOPIC", parameters = listOf("#channel", "another channel topic!")), message)
    }

}