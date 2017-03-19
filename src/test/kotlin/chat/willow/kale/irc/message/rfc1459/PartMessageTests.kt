package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.prefix
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PartMessageTests {

    private lateinit var messageParser: PartMessage.Message.Parser
    private lateinit var messageSerialiser: PartMessage.Command.Serialiser

    @Before fun setUp() {
        messageParser = PartMessage.Message.Parser
        messageSerialiser = PartMessage.Command.Serialiser
    }

    @Test fun test_parse_OneChannel() {
        val message = messageParser.parse(IrcMessage(command = "PART", prefix = "someone", parameters = listOf("channel1")))

        assertEquals(message, PartMessage.Message(source = prefix("someone"), channels = listOf("channel1")))
    }

    @Test fun test_parse_MultipleChannels() {
        val message = messageParser.parse(IrcMessage(command = "PART", prefix = "someone", parameters = listOf("channel1,channel2,channel3")))

        assertEquals(message, PartMessage.Message(source = prefix("someone"), channels = listOf("channel1", "channel2", "channel3")))
    }

    @Test fun test_parse_NoParameters() {
        val message = messageParser.parse(IrcMessage(command = "PART"))

        assertEquals(message, null)
    }

    @Test fun test_parse_SomeoneParted() {
        val message = messageParser.parse(IrcMessage(command = "PART", prefix = "someone@somewhere", parameters = listOf("#channel")))

        assertEquals(PartMessage.Message(source = Prefix(nick = "someone", host = "somewhere"), channels = listOf("#channel")), message)
    }

    @Test fun test_serialise_OneChannel() {
        val message = messageSerialiser.serialise(PartMessage.Command(channels = listOf("channel1")))

        assertEquals(message, IrcMessage(command = "PART", parameters = listOf("channel1")))
    }

    @Test fun test_serialise_MultipleChannels() {
        val message = messageSerialiser.serialise(PartMessage.Command(channels = listOf("channel1", "channel2")))

        assertEquals(message, IrcMessage(command = "PART", parameters = listOf("channel1,channel2")))
    }

}