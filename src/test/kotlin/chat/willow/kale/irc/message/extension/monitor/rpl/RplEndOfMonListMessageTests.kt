package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RplEndOfMonListMessageTests {
    private lateinit var factory: RplEndOfMonListMessage.Factory

    @Before fun setUp() {
        factory = RplEndOfMonListMessage
    }

    @Test fun test_parse_SanityCheck() {
        val message = factory.parse(IrcMessage(command = "733", prefix = "server", parameters = listOf("nick", "message")))

        assertEquals(RplEndOfMonListMessage(prefix = Prefix(nick = "server"), nick = "nick", message = "message"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "733", prefix = "server", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "733", prefix = "server", parameters = listOf("nick")))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_SanityCheck() {
        val message = factory.serialise(RplEndOfMonListMessage(prefix = Prefix(nick = "server"), nick = "nick", message = "message"))

        assertEquals(IrcMessage(command = "733", prefix = "server", parameters = listOf("nick", "message")), message)
    }

}