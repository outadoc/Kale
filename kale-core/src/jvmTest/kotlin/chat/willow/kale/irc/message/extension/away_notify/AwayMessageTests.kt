package chat.willow.kale.irc.message.extension.away_notify

import chat.willow.kale.core.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class AwayMessageTests {

    lateinit var messageParser: AwayMessage.Message.Parser
    lateinit var messageSerialiser: AwayMessage.Message.Serialiser

    @BeforeTest fun setUp() {
        messageParser = AwayMessage.Message.Parser
        messageSerialiser = AwayMessage.Message.Serialiser
    }

    @Test fun test_parse_SourceAndMessage() {
        val message = messageParser.parse(IrcMessage(command = "AWAY", prefix = "nickname", parameters = listOf("test away message")))

        assertEquals(AwayMessage.Message(source = Prefix(nick = "nickname"), message = "test away message"), message)
    }

    @Test fun test_parse_SourceWithoutMessage() {
        val message = messageParser.parse(IrcMessage(command = "AWAY", prefix = "nickname", parameters = listOf()))

        assertEquals(AwayMessage.Message(source = Prefix(nick = "nickname"), message = null), message)
    }

    @Test fun test_parse_MissingPrefix_ReturnsNull() {
        val message = messageParser.parse(IrcMessage(command = "AWAY", parameters = listOf("away")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceAndMessage() {
        val message = messageSerialiser.serialise(AwayMessage.Message(source = Prefix(nick = "nickname"), message = "test away message"))

        assertEquals(IrcMessage(command = "AWAY", prefix = "nickname", parameters = listOf("test away message")), message)
    }

    @Test fun test_serialise_SourceWithoutMessage() {
        val message = messageSerialiser.serialise(AwayMessage.Message(source = Prefix(nick = "nickname"), message = null))

        assertEquals(IrcMessage(command = "AWAY", prefix = "nickname", parameters = listOf()), message)
    }

}