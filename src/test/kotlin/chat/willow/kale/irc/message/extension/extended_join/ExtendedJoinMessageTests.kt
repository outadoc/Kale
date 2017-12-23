package chat.willow.kale.irc.message.extension.extended_join

import chat.willow.kale.generator.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ExtendedJoinMessageTests {

    private lateinit var messageParser: ExtendedJoinMessage.Message.Parser
    private lateinit var messageSerialiser: ExtendedJoinMessage.Message.Serialiser

    @Before fun setUp() {
        messageParser = ExtendedJoinMessage.Message.Parser
        messageSerialiser = ExtendedJoinMessage.Message.Serialiser
    }

    @Test fun test_parse_UserHasAccount_ParsesAsExpected() {
        val message = messageParser.parse(IrcMessage(command = "JOIN", prefix = "nick", parameters = listOf("#channel", "account", "Real Name")))

        assertEquals(ExtendedJoinMessage.Message(source = Prefix(nick = "nick"), channel = "#channel", account = "account", realName = "Real Name"), message)
    }

    @Test fun test_parse_UserDoesNotHaveAccount_ParsesAsExpected() {
        val message = messageParser.parse(IrcMessage(command = "JOIN", prefix = "nick", parameters = listOf("#channel", "*", "Real Name")))

        assertEquals(ExtendedJoinMessage.Message(source = Prefix(nick = "nick"), channel = "#channel", account = null, realName = "Real Name"), message)
    }

    @Test fun test_parse_MissingPrefix_ReturnsNull() {
        val message = messageParser.parse(IrcMessage(command = "JOIN", prefix = null, parameters = listOf("#channel", "account", "Real Name")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters_ReturnsNull() {
        val messageOne = messageParser.parse(IrcMessage(command = "JOIN", prefix = null, parameters = listOf()))
        val messageTwo = messageParser.parse(IrcMessage(command = "JOIN", prefix = null, parameters = listOf("#channel")))
        val messageThree = messageParser.parse(IrcMessage(command = "JOIN", prefix = null, parameters = listOf("#channel", "account")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_MessageHasAccount_SerialisesAsExpected() {
        val message = messageSerialiser.serialise(ExtendedJoinMessage.Message(source = Prefix(nick = "nick"), channel = "#channel", account = "account", realName = "Real Name"))

        assertEquals(IrcMessage(command = "JOIN", prefix = "nick", parameters = listOf("#channel", "account", "Real Name")), message)
    }

    @Test fun test_serialise_MessageDoesNotHaveAccount_SerialisesAsExpected() {
        val message = messageSerialiser.serialise(ExtendedJoinMessage.Message(source = Prefix(nick = "nick"), channel = "#channel", account = null, realName = "Real Name"))

        assertEquals(IrcMessage(command = "JOIN", prefix = "nick", parameters = listOf("#channel", "*", "Real Name")), message)
    }

}