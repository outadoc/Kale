package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.generator.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RplMonListMessageTests {

    private lateinit var messageParser: RplMonList.Message.Parser
    private lateinit var messageSerialiser: RplMonList.Message.Serialiser

    @Before fun setUp() {
        messageParser = RplMonList.Message.Parser
        messageSerialiser = RplMonList.Message.Serialiser
    }

    @Test fun test_parse_ValidPrefix_SingleTarget() {
        val message = messageParser.parse(IrcMessage(command = "732", prefix = "server", parameters = listOf("nick", "someone!user@somewhere")))

        assertEquals(RplMonList.Message(prefix = Prefix(nick = "server"), nick = "nick", targets = listOf(Prefix(nick = "someone", user = "user", host = "somewhere"))), message)
    }

    @Test fun test_parse_ValidPrefix_MultipleTargets() {
        val message = messageParser.parse(IrcMessage(command = "732", prefix = "server", parameters = listOf("nick", "someone!user@somewhere,someone-else")))

        assertEquals(RplMonList.Message(prefix = Prefix(nick = "server"), nick = "nick", targets = listOf(Prefix(nick = "someone", user = "user", host = "somewhere"), Prefix(nick = "someone-else"))), message)
    }

    @Test fun test_parse_MissingPrefix() {
        val message = messageParser.parse(IrcMessage(command = "732", prefix = null, parameters = listOf("nick", "someone!user@somewhere,someone-else")))

        assertNull(message)
    }

    @Test fun test_parse_InvalidPrefix() {
        val message = messageParser.parse(IrcMessage(command = "732", prefix = "!!!", parameters = listOf("nick", "someone!user@somewhere,someone-else")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "732", prefix = "server", parameters = listOf("*")))
        val messageTwo = messageParser.parse(IrcMessage(command = "732", prefix = "server", parameters = listOf()))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_SingleTarget() {
        val message = messageSerialiser.serialise(RplMonList.Message(prefix = Prefix(nick = "server"), nick = "nick", targets = listOf(Prefix(nick = "someone"))))

        assertEquals(IrcMessage(command = "732", prefix = "server", parameters = listOf("nick", "someone")), message)
    }

    @Test fun test_serialise_MultipleTargets() {
        val message = messageSerialiser.serialise(RplMonList.Message(prefix = Prefix(nick = "server"), nick = "nick", targets = listOf(Prefix(nick = "someone"), Prefix(nick = "someone-else", user = "user", host = "somewhere"))))

        assertEquals(IrcMessage(command = "732", prefix = "server", parameters = listOf("nick", "someone,someone-else!user@somewhere")), message)
    }

}