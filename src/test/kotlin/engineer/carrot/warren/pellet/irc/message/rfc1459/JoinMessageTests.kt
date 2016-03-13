package engineer.carrot.warren.pellet.irc.message.rfc1459

import engineer.carrot.warren.pellet.irc.message.IMessageFactory
import engineer.carrot.warren.pellet.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class JoinMessageTests {
    lateinit var factory: IMessageFactory<JoinMessage>

    @Before fun setUp() {
        factory = JoinMessage.Factory
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

}