package chat.willow.kale.irc.message.extension.away_notify

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AwayMessageTests {

    lateinit var factory: AwayMessage.Factory

    @Before fun setUp() {
        factory = AwayMessage
    }

    @Test fun test_parse_SourceAndMessage() {
        val message = factory.parse(IrcMessage(command = "AWAY", prefix = "nickname", parameters = listOf("test away message")))

        assertEquals(AwayMessage(source = Prefix(nick = "nickname"), message = "test away message"), message)
    }

    @Test fun test_parse_SourceWithoutMessage() {
        val message = factory.parse(IrcMessage(command = "AWAY", prefix = "nickname", parameters = listOf()))

        assertEquals(AwayMessage(source = Prefix(nick = "nickname"), message = null), message)
    }

    @Test fun test_parse_MissingPrefix_ReturnsNull() {
        val message = factory.parse(IrcMessage(command = "AWAY", parameters = listOf("away")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceAndMessage() {
        val message = factory.serialise(AwayMessage(source = Prefix(nick = "nickname"), message = "test away message"))

        assertEquals(IrcMessage(command = "AWAY", prefix = "nickname", parameters = listOf("test away message")), message)
    }

    @Test fun test_serialise_SourceWithoutMessage() {
        val message = factory.serialise(AwayMessage(source = Prefix(nick = "nickname"), message = null))

        assertEquals(IrcMessage(command = "AWAY", prefix = "nickname", parameters = listOf()), message)
    }

}