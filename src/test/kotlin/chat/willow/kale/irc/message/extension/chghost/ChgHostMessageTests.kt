package chat.willow.kale.irc.message.extension.chghost

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ChgHostMessageTests {

    private lateinit var sut: ChgHostMessage.Factory

    @Before fun setUp() {
        sut = ChgHostMessage
    }

    @Test fun test_parse_SanityCheck() {
        val message = sut.parse(IrcMessage(command = "CHGHOST", prefix = "testnick!testuser@testhost", parameters = listOf("newuser", "newhost")))

        assertEquals(ChgHostMessage(source = Prefix(nick = "testnick", user = "testuser", host = "testhost"), newUser = "newuser", newHost = "newhost"), message)
    }

    @Test fun test_parse_TooFewParameters_ReturnsNull() {
        val messageOne = sut.parse(IrcMessage(command = "CHGHOST", prefix = "testnick!testuser@testhost", parameters = listOf("newuser")))
        val messageTwo = sut.parse(IrcMessage(command = "CHGHOST", prefix = "testnick!testuser@testhost", parameters = listOf()))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_parse_MissingPrefix_ReturnsNull() {
        val message = sut.parse(IrcMessage(command = "CHGHOST", prefix = null, parameters = listOf("newuser", "newhost")))
    }

    @Test fun test_serialise_SanityCheck() {
        val message = sut.serialise(ChgHostMessage(source = Prefix(nick = "testnick", user = "testuser", host = "testhost"), newUser = "newuser", newHost = "newhost"))

        assertEquals(IrcMessage(command = "CHGHOST", prefix = "testnick!testuser@testhost", parameters = listOf("newuser", "newhost")), message)
    }

}