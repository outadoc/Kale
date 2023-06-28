package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.core.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class RplEndOfMonListMessageTests {

    private lateinit var messageParser: RplEndOfMonList.Message.Parser
    private lateinit var messageSerialiser: RplEndOfMonList.Message.Serialiser

    @BeforeTest fun setUp() {
        messageParser = RplEndOfMonList.Message.Parser
        messageSerialiser = RplEndOfMonList.Message.Serialiser
    }

    @Test fun test_parse_SanityCheck() {
        val message = messageParser.parse(IrcMessage(command = "733", prefix = "server", parameters = listOf("nick", "message")))

        assertEquals(RplEndOfMonList.Message(prefix = Prefix(nick = "server"), nick = "nick", message = "message"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "733", prefix = "server", parameters = listOf()))
        val messageTwo = messageParser.parse(IrcMessage(command = "733", prefix = "server", parameters = listOf("nick")))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_SanityCheck() {
        val message = messageSerialiser.serialise(RplEndOfMonList.Message(prefix = Prefix(nick = "server"), nick = "nick", message = "message"))

        assertEquals(IrcMessage(command = "733", prefix = "server", parameters = listOf("nick", "message")), message)
    }

}