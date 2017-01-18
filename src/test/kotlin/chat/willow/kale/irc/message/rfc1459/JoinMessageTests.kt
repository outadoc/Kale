package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class JoinMessageTests {
    lateinit var factory: JoinMessage.Factory

    @Before fun setUp() {
        factory = JoinMessage
    }

    @Test fun test_parse_OneChannel() {
        val message = factory.parse(IrcMessage(command = "JOIN", parameters = listOf("channel1")))

        assertEquals(message, JoinMessage(channels = listOf("channel1")))
    }

    @Test fun test_parse_MultipleChannels() {
        val message = factory.parse(IrcMessage(command = "JOIN", parameters = listOf("channel1,channel2,channel3")))

        assertEquals(message, JoinMessage(channels = listOf("channel1", "channel2", "channel3")))
    }

    @Test fun test_parse_OneChannelAndKey() {
        val message = factory.parse(IrcMessage(command = "JOIN", parameters = listOf("channel1", "key1")))

        assertEquals(message, JoinMessage(channels = listOf("channel1"), keys = listOf("key1")))
    }

    @Test fun test_parse_MultipleChannels_OneKey() {
        val message = factory.parse(IrcMessage(command = "JOIN", parameters = listOf("channel1,channel2,channel3", "key1")))

        assertEquals(message, JoinMessage(channels = listOf("channel1", "channel2", "channel3"), keys = listOf("key1")))
    }

    @Test fun test_parse_MultipleChannels_MultipleKeys() {
        val message = factory.parse(IrcMessage(command = "JOIN", parameters = listOf("channel1,channel2", "key1,key2")))

        assertEquals(message, JoinMessage(channels = listOf("channel1", "channel2"), keys = listOf("key1", "key2")))
    }

    @Test fun test_parse_NoParameters() {
        val message = factory.parse(IrcMessage(command = "JOIN"))

        assertNull(message)
    }

    @Test fun test_parse_SomeoneJoiningAChannel() {
        val message = factory.parse(IrcMessage(command = "JOIN", prefix = "someone@somewhere", parameters = listOf("#channel")))

        assertEquals(JoinMessage(source = Prefix(nick = "someone", host = "somewhere"), channels = listOf("#channel")), message)
    }

    @Test fun test_serialise_MultipleChannels() {
        val message = factory.serialise(JoinMessage(channels = listOf("channel1", "channel2")))

        assertEquals(message, IrcMessage(command = "JOIN", parameters = listOf("channel1,channel2")))
    }

    @Test fun test_serialise_MultipleChannels_OneKey() {
        val message = factory.serialise(JoinMessage(channels = listOf("channel1", "channel2"), keys = listOf("key1")))

        assertEquals(message, IrcMessage(command = "JOIN", parameters = listOf("channel1,channel2", "key1")))
    }

    @Test fun test_serialise_MultipleChannels_MultipleKeys() {
        val message = factory.serialise(JoinMessage(channels = listOf("channel1", "channel2", "channel3"), keys = listOf("key1", "key2")))

        assertEquals(message, IrcMessage(command = "JOIN", parameters = listOf("channel1,channel2,channel3", "key1,key2")))
    }

    @Test fun test_serialise_SomeoneJoiningAChannel() {
        val message = factory.serialise(JoinMessage(source = Prefix(nick = "someone", host = "somewhere"), channels = listOf("#channel")))

        assertEquals(IrcMessage(command = "JOIN", prefix = "someone@somewhere", parameters = listOf("#channel")), message)
    }

}