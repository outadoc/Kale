package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.core.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RplMonOfflineMessageTests {

    private lateinit var messageParser: RplMonOffline.Message.Parser
    private lateinit var messageSerialiser: RplMonOffline.Message.Serialiser

    @Before fun setUp() {
        messageParser = RplMonOffline.Message.Parser
        messageSerialiser = RplMonOffline.Message.Serialiser
    }

    @Test fun test_parse_ValidPrefix_SingleTarget() {
        val message = messageParser.parse(IrcMessage(command = "731", prefix = "server", parameters = listOf("*", "someone!user@somewhere")))

        assertEquals(RplMonOffline.Message(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone", user = "user", host = "somewhere"))), message)
    }

    @Test fun test_parse_ValidPrefix_MultipleTargets() {
        val message = messageParser.parse(IrcMessage(command = "731", prefix = "server", parameters = listOf("*", "someone!user@somewhere,someone-else")))

        assertEquals(RplMonOffline.Message(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone", user = "user", host = "somewhere"), Prefix(nick = "someone-else"))), message)
    }

    @Test fun test_parse_MissingPrefix() {
        val message = messageParser.parse(IrcMessage(command = "731", prefix = null, parameters = listOf("*", "someone!user@somewhere,someone-else")))

        assertNull(message)
    }

    @Test fun test_parse_InvalidPrefix() {
        val message = messageParser.parse(IrcMessage(command = "731", prefix = "!!!", parameters = listOf("*", "someone!user@somewhere,someone-else")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "731", prefix = "server", parameters = listOf("*")))
        val messageTwo = messageParser.parse(IrcMessage(command = "731", prefix = "server", parameters = listOf()))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_SingleTarget() {
        val message = messageSerialiser.serialise(RplMonOffline.Message(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone"))))

        assertEquals(IrcMessage(command = "731", prefix = "server", parameters = listOf("*", "someone")), message)
    }

    @Test fun test_serialise_MultipleTargets() {
        val message = messageSerialiser.serialise(RplMonOffline.Message(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone"), Prefix(nick = "someone-else", user = "user", host = "somewhere"))))

        assertEquals(IrcMessage(command = "731", prefix = "server", parameters = listOf("*", "someone,someone-else!user@somewhere")), message)
    }

}