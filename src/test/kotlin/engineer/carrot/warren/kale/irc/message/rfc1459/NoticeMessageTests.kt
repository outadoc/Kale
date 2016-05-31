package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class NoticeMessageTests {
    lateinit var factory: NoticeMessage.Factory

    @Before fun setUp() {
        factory = NoticeMessage.Factory
    }

    @Test fun test_parse_MessageFromUser() {
        val message = factory.parse(IrcMessage(command = "NOTICE", prefix = "Angel", parameters = listOf("Wiz", "Hello are you receiving this message ?")))

        assertEquals(message, NoticeMessage(source = Prefix(nick = "Angel"), target = "Wiz", message = "Hello are you receiving this message ?"))
    }

    @Test fun test_parse_MessageToUser() {
        val message = factory.parse(IrcMessage(command = "NOTICE", parameters = listOf("Angel", "yes I'm receiving it !")))

        assertEquals(message, NoticeMessage(target = "Angel", message = "yes I'm receiving it !"))
    }

    @Test fun test_parse_MessageToHostmask() {
        val message = factory.parse(IrcMessage(command = "NOTICE", parameters = listOf("jto@tolsun.oulu.fi", "Hello !")))

        assertEquals(message, NoticeMessage(target = "jto@tolsun.oulu.fi", message = "Hello !"))
    }

    @Test fun test_parse_MessageToServerWildcard() {
        val message = factory.parse(IrcMessage(command = "NOTICE", parameters = listOf("$*.fi", "Server tolsun.oulu.fi rebooting.")))

        assertEquals(message, NoticeMessage(target = "$*.fi", message = "Server tolsun.oulu.fi rebooting."))
    }

    @Test fun test_parse_MessageToHostWildcard() {
        val message = factory.parse(IrcMessage(command = "NOTICE", parameters = listOf("#*.edu", "NSFNet is undergoing work, expect interruptions")))

        assertEquals(message, NoticeMessage(target = "#*.edu", message = "NSFNet is undergoing work, expect interruptions"))
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "NOTICE", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "NOTICE", parameters = listOf("test")))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_MessageFromUser() {
        val message = factory.serialise(NoticeMessage(source = Prefix(nick = "Angel"), target = "Wiz", message = "Hello are you receiving this message ?"))

        assertEquals(message, IrcMessage(command = "NOTICE", prefix = "Angel", parameters = listOf("Wiz", "Hello are you receiving this message ?")))
    }

    @Test fun test_serialise_MessageToUser() {
        val message = factory.serialise(NoticeMessage(target = "Angel", message = "yes I'm receiving it !"))

        assertEquals(message, IrcMessage(command = "NOTICE", parameters = listOf("Angel", "yes I'm receiving it !")))
    }

    @Test fun test_serialise_MessageToHostmask() {
        val message = factory.serialise(NoticeMessage(target = "jto@tolsun.oulu.fi", message = "Hello !"))

        assertEquals(message, IrcMessage(command = "NOTICE", parameters = listOf("jto@tolsun.oulu.fi", "Hello !")))
    }

    @Test fun test_serialise_MessageToServerWildcard() {
        val message = factory.serialise(NoticeMessage(target = "$*.fi", message = "Server tolsun.oulu.fi rebooting."))

        assertEquals(message, IrcMessage(command = "NOTICE", parameters = listOf("$*.fi", "Server tolsun.oulu.fi rebooting.")))
    }

    @Test fun test_serialise_MessageToHostWildcard() {
        val message = factory.serialise(NoticeMessage(target = "#*.edu", message = "NSFNet is undergoing work, expect interruptions"))

        assertEquals(message, IrcMessage(command = "NOTICE", parameters = listOf("#*.edu", "NSFNet is undergoing work, expect interruptions")))
    }
}