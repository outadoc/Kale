package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.generator.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class JoinMessageTests {

    private lateinit var messageParser: JoinMessage.Message.Parser
    private lateinit var messageSerialiser: JoinMessage.Command.Serialiser

    @Before fun setUp() {
        messageParser = JoinMessage.Message.Parser
        messageSerialiser = JoinMessage.Command.Serialiser
    }

    @Test fun test_parse_OneChannel() {
        val message = messageParser.parse(IrcMessage(command = "JOIN", prefix = "someone", parameters = listOf("channel1")))

        assertEquals(message, JoinMessage.Message(source = prefix("someone"), channels = listOf("channel1")))
    }

    @Test fun test_parse_MultipleChannels() {
        val message = messageParser.parse(IrcMessage(command = "JOIN", prefix = "someone", parameters = listOf("channel1,channel2,channel3")))

        assertEquals(message, JoinMessage.Message(source = prefix("someone"), channels = listOf("channel1", "channel2", "channel3")))
    }

    @Test fun test_parse_NoParameters() {
        val message = messageParser.parse(IrcMessage(command = "JOIN"))

        assertNull(message)
    }

    @Test fun test_parse_SomeoneJoiningAChannel() {
        val message = messageParser.parse(IrcMessage(command = "JOIN", prefix = "someone@somewhere", parameters = listOf("#channel")))

        assertEquals(JoinMessage.Message(source = Prefix(nick = "someone", host = "somewhere"), channels = listOf("#channel")), message)
    }

    @Test fun test_serialise_MultipleChannels() {
        val message = messageSerialiser.serialise(JoinMessage.Command(channels = listOf("channel1", "channel2")))

        assertEquals(message, IrcMessage(command = "JOIN", parameters = listOf("channel1,channel2")))
    }

    @Test fun test_serialise_MultipleChannels_OneKey() {
        val message = messageSerialiser.serialise(JoinMessage.Command(channels = listOf("channel1", "channel2"), keys = listOf("key1")))

        assertEquals(message, IrcMessage(command = "JOIN", parameters = listOf("channel1,channel2", "key1")))
    }

    @Test fun test_serialise_MultipleChannels_MultipleKeys() {
        val message = messageSerialiser.serialise(JoinMessage.Command(channels = listOf("channel1", "channel2", "channel3"), keys = listOf("key1", "key2")))

        assertEquals(message, IrcMessage(command = "JOIN", parameters = listOf("channel1,channel2,channel3", "key1,key2")))
    }

}