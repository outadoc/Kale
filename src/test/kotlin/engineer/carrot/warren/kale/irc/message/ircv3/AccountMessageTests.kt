package engineer.carrot.warren.kale.irc.message.ircv3

import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AccountMessageTests {

    lateinit var factory: AccountMessage.Factory

    @Before fun setUp() {
        factory = AccountMessage
    }

    @Test fun test_parse_SourceAndAccount() {
        val message = factory.parse(IrcMessage(command = "ACCOUNT", prefix = "nickname", parameters = listOf("account")))

        assertEquals(AccountMessage(source = Prefix(nick = "nickname"), account = "account"), message)
    }

    @Test fun test_parse_MissingPrefix_ReturnsNull() {
        val message = factory.parse(IrcMessage(command = "ACCOUNT", parameters = listOf("account")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "ACCOUNT", prefix = "nickname"))

        assertNull(messageOne)
    }

    @Test fun test_serialise_Source_Account() {
        val message = factory.serialise(AccountMessage(source = Prefix(nick = "nickname"), account = "account"))

        assertEquals(IrcMessage(command = "ACCOUNT", prefix = "nickname", parameters = listOf("account")), message)
    }

}