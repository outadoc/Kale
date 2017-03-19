package chat.willow.kale.irc.message.extension.chghost

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ChgHostMessageTests {

    private lateinit var messageParser: ChgHostMessage.Message.Parser
    private lateinit var messageSerialiser: ChgHostMessage.Message.Serialiser

    @Before fun setUp() {
        messageParser = ChgHostMessage.Message.Parser
        messageSerialiser = ChgHostMessage.Message.Serialiser
    }

    @Test fun test_parse_SanityCheck() {
        val message = messageParser.parse(IrcMessage(command = "CHGHOST", prefix = "testnick!testuser@testhost", parameters = listOf("newuser", "newhost")))

        assertEquals(ChgHostMessage.Message(source = Prefix(nick = "testnick", user = "testuser", host = "testhost"), newUser = "newuser", newHost = "newhost"), message)
    }

    @Test fun test_parse_TooFewParameters_ReturnsNull() {
        val messageOne = messageParser.parse(IrcMessage(command = "CHGHOST", prefix = "testnick!testuser@testhost", parameters = listOf("newuser")))
        val messageTwo = messageParser.parse(IrcMessage(command = "CHGHOST", prefix = "testnick!testuser@testhost", parameters = listOf()))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_parse_MissingPrefix_ReturnsNull() {
        val message = messageParser.parse(IrcMessage(command = "CHGHOST", prefix = null, parameters = listOf("newuser", "newhost")))

        assertNull(message)
    }

    @Test fun test_serialise_SanityCheck() {
        val message = messageSerialiser.serialise(ChgHostMessage.Message(source = Prefix(nick = "testnick", user = "testuser", host = "testhost"), newUser = "newuser", newHost = "newhost"))

        assertEquals(IrcMessage(command = "CHGHOST", prefix = "testnick!testuser@testhost", parameters = listOf("newuser", "newhost")), message)
    }

}