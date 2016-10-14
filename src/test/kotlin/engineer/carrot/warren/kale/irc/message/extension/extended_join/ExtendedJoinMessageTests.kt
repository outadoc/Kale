package engineer.carrot.warren.kale.irc.message.extension.extended_join

import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.message.extension.extended_join.ExtendedJoinMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ExtendedJoinMessageTests {
    lateinit var factory: ExtendedJoinMessage.Factory

    @Before fun setUp() {
        factory = ExtendedJoinMessage
    }

    @Test fun test_parse_UserHasAccount_ParsesAsExpected() {
        val message = factory.parse(IrcMessage(command = "JOIN", prefix = "nick", parameters = listOf("#channel", "account", "Real Name")))

        assertEquals(ExtendedJoinMessage(source = Prefix(nick = "nick"), channel = "#channel", account = "account", realName = "Real Name"), message)
    }

    @Test fun test_parse_UserDoesNotHaveAccount_ParsesAsExpected() {
        val message = factory.parse(IrcMessage(command = "JOIN", prefix = "nick", parameters = listOf("#channel", "*", "Real Name")))

        assertEquals(ExtendedJoinMessage(source = Prefix(nick = "nick"), channel = "#channel", account = null, realName = "Real Name"), message)
    }

    @Test fun test_parse_MissingPrefix_ReturnsNull() {
        val message = factory.parse(IrcMessage(command = "JOIN", prefix = null, parameters = listOf("#channel", "account", "Real Name")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters_ReturnsNull() {
        val messageOne = factory.parse(IrcMessage(command = "JOIN", prefix = null, parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "JOIN", prefix = null, parameters = listOf("#channel")))
        val messageThree = factory.parse(IrcMessage(command = "JOIN", prefix = null, parameters = listOf("#channel", "account")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_MessageHasAccount_SerialisesAsExpected() {
        val message = factory.serialise(ExtendedJoinMessage(source = Prefix(nick = "nick"), channel = "#channel", account = "account", realName = "Real Name"))

        assertEquals(IrcMessage(command = "JOIN", prefix = "nick", parameters = listOf("#channel", "account", "Real Name")), message)
    }

    @Test fun test_serialise_MessageDoesNotHaveAccount_SerialisesAsExpected() {
        val message = factory.serialise(ExtendedJoinMessage(source = Prefix(nick = "nick"), channel = "#channel", account = null, realName = "Real Name"))

        assertEquals(IrcMessage(command = "JOIN", prefix = "nick", parameters = listOf("#channel", "*", "Real Name")), message)
    }

}