package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class PrivMsgMessageTests {
    lateinit var factory: IMessageFactory<PrivMsgMessage>

    @Before fun setUp() {
        factory = PrivMsgMessage.Factory
    }

    @Test fun test_parse_MessageFromUser() {
        val message = factory.parse(IrcMessage(command = "PRIVMSG", prefix = "Angel", parameters = listOf("Wiz", "Hello are you receiving this message ?")))

        assertEquals(message, PrivMsgMessage(source = "Angel", target = "Wiz", message = "Hello are you receiving this message ?"))
    }

    @Test fun test_parse_MessageToUser() {
        val message = factory.parse(IrcMessage(command = "PRIVMSG", parameters = listOf("Angel", "yes I'm receiving it !")))

        assertEquals(message, PrivMsgMessage(target = "Angel", message = "yes I'm receiving it !"))
    }

    @Test fun test_parse_MessageToHostmask() {
        val message = factory.parse(IrcMessage(command = "PRIVMSG", parameters = listOf("jto@tolsun.oulu.fi", "Hello !")))

        assertEquals(message, PrivMsgMessage(target = "jto@tolsun.oulu.fi", message = "Hello !"))
    }

    @Test fun test_parse_MessageToServerWildcard() {
        val message = factory.parse(IrcMessage(command = "PRIVMSG", parameters = listOf("$*.fi", "Server tolsun.oulu.fi rebooting.")))

        assertEquals(message, PrivMsgMessage(target = "$*.fi", message = "Server tolsun.oulu.fi rebooting."))
    }

    @Test fun test_parse_MessageToHostWildcard() {
        val message = factory.parse(IrcMessage(command = "PRIVMSG", parameters = listOf("#*.edu", "NSFNet is undergoing work, expect interruptions")))

        assertEquals(message, PrivMsgMessage(target = "#*.edu", message = "NSFNet is undergoing work, expect interruptions"))
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "PRIVMSG", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "PRIVMSG", parameters = listOf("test")))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_MessageFromUser() {
        val message = factory.serialise(PrivMsgMessage(source = "Angel", target = "Wiz", message = "Hello are you receiving this message ?"))

        assertEquals(message, IrcMessage(command = "PRIVMSG", prefix = "Angel", parameters = listOf("Wiz", "Hello are you receiving this message ?")))
    }

    @Test fun test_serialise_MessageToUser() {
        val message = factory.serialise(PrivMsgMessage(target = "Angel", message = "yes I'm receiving it !"))

        assertEquals(message, IrcMessage(command = "PRIVMSG", parameters = listOf("Angel", "yes I'm receiving it !")))
    }

    @Test fun test_serialise_MessageToHostmask() {
        val message = factory.serialise(PrivMsgMessage(target = "jto@tolsun.oulu.fi", message = "Hello !"))

        assertEquals(message, IrcMessage(command = "PRIVMSG", parameters = listOf("jto@tolsun.oulu.fi", "Hello !")))
    }

    @Test fun test_serialise_MessageToServerWildcard() {
        val message = factory.serialise(PrivMsgMessage(target = "$*.fi", message = "Server tolsun.oulu.fi rebooting."))

        assertEquals(message, IrcMessage(command = "PRIVMSG", parameters = listOf("$*.fi", "Server tolsun.oulu.fi rebooting.")))
    }

    @Test fun test_serialise_MessageToHostWildcard() {
        val message = factory.serialise(PrivMsgMessage(target = "#*.edu", message = "NSFNet is undergoing work, expect interruptions"))

        assertEquals(message, IrcMessage(command = "PRIVMSG", parameters = listOf("#*.edu", "NSFNet is undergoing work, expect interruptions")))
    }
}