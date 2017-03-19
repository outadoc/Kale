package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class PrivMsgMessageTests {

    private lateinit var messageParser: PrivMsgMessage.Message.Parser
    private lateinit var messageSerialiser: PrivMsgMessage.Command.Serialiser

    @Before fun setUp() {
        messageParser = PrivMsgMessage.Message.Parser
        messageSerialiser = PrivMsgMessage.Command.Serialiser
    }

    @Test fun test_parse_MessageFromUser() {
        val message = messageParser.parse(IrcMessage(command = "PRIVMSG", prefix = "Angel", parameters = listOf("Wiz", "Hello are you receiving this message ?")))

        assertEquals(message, PrivMsgMessage.Message(source = Prefix(nick = "Angel"), target = "Wiz", message = "Hello are you receiving this message ?"))
    }

    @Test fun test_parse_MessageToUser() {
        val message = messageParser.parse(IrcMessage(command = "PRIVMSG", prefix = "someone", parameters = listOf("Angel", "yes I'm receiving it !")))

        assertEquals(message, PrivMsgMessage.Message(source = prefix("someone"), target = "Angel", message = "yes I'm receiving it !"))
    }

    @Test fun test_parse_MessageToHostmask() {
        val message = messageParser.parse(IrcMessage(command = "PRIVMSG", prefix = "someone", parameters = listOf("jto@tolsun.oulu.fi", "Hello !")))

        assertEquals(message, PrivMsgMessage.Message(source = prefix("someone"), target = "jto@tolsun.oulu.fi", message = "Hello !"))
    }

    @Test fun test_parse_MessageToServerWildcard() {
        val message = messageParser.parse(IrcMessage(command = "PRIVMSG", prefix = "someone", parameters = listOf("$*.fi", "Server tolsun.oulu.fi rebooting.")))

        assertEquals(message, PrivMsgMessage.Message(source = prefix("someone"), target = "$*.fi", message = "Server tolsun.oulu.fi rebooting."))
    }

    @Test fun test_parse_MessageToHostWildcard() {
        val message = messageParser.parse(IrcMessage(command = "PRIVMSG", prefix = "someone", parameters = listOf("#*.edu", "NSFNet is undergoing work, expect interruptions")))

        assertEquals(message, PrivMsgMessage.Message(source = prefix("someone"), target = "#*.edu", message = "NSFNet is undergoing work, expect interruptions"))
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "PRIVMSG", parameters = listOf()))
        val messageTwo = messageParser.parse(IrcMessage(command = "PRIVMSG", parameters = listOf("test")))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_MessageToUser() {
        val message = messageSerialiser.serialise(PrivMsgMessage.Command(target = "Angel", message = "yes I'm receiving it !"))

        assertEquals(message, IrcMessage(command = "PRIVMSG", parameters = listOf("Angel", "yes I'm receiving it !")))
    }

    @Test fun test_serialise_MessageToHostmask() {
        val message = messageSerialiser.serialise(PrivMsgMessage.Command(target = "jto@tolsun.oulu.fi", message = "Hello !"))

        assertEquals(message, IrcMessage(command = "PRIVMSG", parameters = listOf("jto@tolsun.oulu.fi", "Hello !")))
    }

    @Test fun test_serialise_MessageToServerWildcard() {
        val message = messageSerialiser.serialise(PrivMsgMessage.Command(target = "$*.fi", message = "Server tolsun.oulu.fi rebooting."))

        assertEquals(message, IrcMessage(command = "PRIVMSG", parameters = listOf("$*.fi", "Server tolsun.oulu.fi rebooting.")))
    }

    @Test fun test_serialise_MessageToHostWildcard() {
        val message = messageSerialiser.serialise(PrivMsgMessage.Command(target = "#*.edu", message = "NSFNet is undergoing work, expect interruptions"))

        assertEquals(message, IrcMessage(command = "PRIVMSG", parameters = listOf("#*.edu", "NSFNet is undergoing work, expect interruptions")))
    }
}