package chat.willow.kale.irc.message.extension.account_notify

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AccountMessageTests {

    lateinit var messageParser: AccountMessage.Message.Parser
    lateinit var messageSerialiser: AccountMessage.Message.Serialiser

    @Before fun setUp() {
        messageParser = AccountMessage.Message.Parser
        messageSerialiser = AccountMessage.Message.Serialiser
    }

    @Test fun test_parse_SourceAndAccount() {
        val message = messageParser.parse(IrcMessage(command = "ACCOUNT", prefix = "nickname", parameters = listOf("account")))

        assertEquals(AccountMessage.Message(source = Prefix(nick = "nickname"), account = "account"), message)
    }

    @Test fun test_parse_MissingPrefix_ReturnsNull() {
        val message = messageParser.parse(IrcMessage(command = "ACCOUNT", parameters = listOf("account")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "ACCOUNT", prefix = "nickname"))

        assertNull(messageOne)
    }

    @Test fun test_serialise_Source_Account() {
        val message = messageSerialiser.serialise(AccountMessage.Message(source = Prefix(nick = "nickname"), account = "account"))

        assertEquals(IrcMessage(command = "ACCOUNT", prefix = "nickname", parameters = listOf("account")), message)
    }

}