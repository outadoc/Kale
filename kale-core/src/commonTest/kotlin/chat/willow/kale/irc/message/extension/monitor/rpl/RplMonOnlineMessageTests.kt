package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.core.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class RplMonOnlineMessageTests {

    private lateinit var messageParser: RplMonOnline.Message.Parser
    private lateinit var messageSerialiser: RplMonOnline.Message.Serialiser

    @BeforeTest fun setUp() {
        messageParser = RplMonOnline.Message.Parser
        messageSerialiser = RplMonOnline.Message.Serialiser
    }

    @Test fun test_parse_ValidPrefix_SingleTarget() {
        val message = messageParser.parse(IrcMessage(command = "730", prefix = "server", parameters = listOf("*", "someone!user@somewhere")))

        assertEquals(RplMonOnline.Message(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone", user = "user", host = "somewhere"))), message)
    }

    @Test fun test_parse_ValidPrefix_MultipleTargets() {
        val message = messageParser.parse(IrcMessage(command = "730", prefix = "server", parameters = listOf("*", "someone!user@somewhere,someone-else")))

        assertEquals(RplMonOnline.Message(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone", user = "user", host = "somewhere"), Prefix(nick = "someone-else"))), message)
    }

    @Test fun test_parse_MissingPrefix() {
        val message = messageParser.parse(IrcMessage(command = "730", prefix = null, parameters = listOf("*", "someone!user@somewhere,someone-else")))

        assertNull(message)
    }

    @Test fun test_parse_InvalidPrefix() {
        val message = messageParser.parse(IrcMessage(command = "730", prefix = "!!!", parameters = listOf("*", "someone!user@somewhere,someone-else")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "730", prefix = "server", parameters = listOf("*")))
        val messageTwo = messageParser.parse(IrcMessage(command = "730", prefix = "server", parameters = listOf()))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_SingleTarget() {
        val message = messageSerialiser.serialise(RplMonOnline.Message(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone"))))

        assertEquals(IrcMessage(command = "730", prefix = "server", parameters = listOf("*", "someone")), message)
    }

    @Test fun test_serialise_MultipleTargets() {
        val message = messageSerialiser.serialise(RplMonOnline.Message(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone"), Prefix(nick = "someone-else", user = "user", host = "somewhere"))))

        assertEquals(IrcMessage(command = "730", prefix = "server", parameters = listOf("*", "someone,someone-else!user@somewhere")), message)
    }

}