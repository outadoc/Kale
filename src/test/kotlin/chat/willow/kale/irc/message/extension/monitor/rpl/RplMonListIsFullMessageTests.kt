package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.generator.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RplMonListIsFullMessageTests {

    private lateinit var messageParser: RplMonListIsFull.Message.Parser
    private lateinit var messageSerialiser: RplMonListIsFull.Message.Serialiser

    @Before fun setUp() {
        messageParser = RplMonListIsFull.Message.Parser
        messageSerialiser = RplMonListIsFull.Message.Serialiser
    }

    @Test fun test_parse_ValidPrefix_SingleTarget() {
        val message = messageParser.parse(IrcMessage(command = "734", prefix = "server", parameters = listOf("nick", "100", "someone!user@somewhere", "message")))

        assertEquals(RplMonListIsFull.Message(prefix = Prefix(nick = "server"), nick = "nick", limit = "100", targets = listOf(Prefix(nick = "someone", user = "user", host = "somewhere")), message = "message"), message)
    }

    @Test fun test_parse_ValidPrefix_MultipleTargets() {
        val message = messageParser.parse(IrcMessage(command = "734", prefix = "server", parameters = listOf("nick", "100", "someone!user@somewhere,someone-else", "message")))

        assertEquals(RplMonListIsFull.Message(prefix = Prefix(nick = "server"), nick = "nick", limit = "100", targets = listOf(Prefix(nick = "someone", user = "user", host = "somewhere"), Prefix(nick = "someone-else")), message = "message"), message)
    }

    @Test fun test_parse_MissingPrefix() {
        val message = messageParser.parse(IrcMessage(command = "734", prefix = null, parameters = listOf("nick", "100", "someone!user@somewhere,someone-else", "message")))

        assertNull(message)
    }

    @Test fun test_parse_InvalidPrefix() {
        val message = messageParser.parse(IrcMessage(command = "734", prefix = "!!!", parameters = listOf("nick", "100", "someone!user@somewhere,someone-else", "message")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "734", prefix = "server", parameters = listOf("*")))
        val messageTwo = messageParser.parse(IrcMessage(command = "734", prefix = "server", parameters = listOf()))
        val messageThree = messageParser.parse(IrcMessage(command = "734", prefix = "server", parameters = listOf("nick", "100")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_SingleTarget() {
        val message = messageSerialiser.serialise(RplMonListIsFull.Message(prefix = Prefix(nick = "server"), nick = "nick", limit = "100", targets = listOf(Prefix(nick = "someone")), message = "message"))

        assertEquals(IrcMessage(command = "734", prefix = "server", parameters = listOf("nick", "100", "someone", "message")), message)
    }

    @Test fun test_serialise_MultipleTargets() {
        val message = messageSerialiser.serialise(RplMonListIsFull.Message(prefix = Prefix(nick = "server"), nick = "nick", limit = "100", targets = listOf(Prefix(nick = "someone"), Prefix(nick = "someone-else", user = "user", host = "somewhere")), message = "message"))

        assertEquals(IrcMessage(command = "734", prefix = "server", parameters = listOf("nick", "100", "someone,someone-else!user@somewhere", "message")), message)
    }

}