package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class NickMessageTests {
    lateinit var factory: NickMessage.Factory

    @Before fun setUp() {
        factory = NickMessage.Factory
    }

    @Test fun test_parse_nicknameOnly() {
        val message = factory.parse(IrcMessage(command = "NICK", parameters = listOf("test_nickname")))

        assertEquals(message, NickMessage(nickname = "test_nickname"))
    }

    @Test fun test_parse_nicknameAndHopcount() {
        val message = factory.parse(IrcMessage(command = "NICK", parameters = listOf("test_nickname", "1")))

        assertEquals(message, NickMessage(nickname = "test_nickname", hopcount = 1))
    }

    @Test fun test_parse_threeParameters() {
        val message = factory.parse(IrcMessage(command = "NICK", parameters = listOf("1", "2", "3")))

        assertEquals(message, NickMessage(nickname = "1", hopcount = 2))
    }

    @Test fun test_parse_WithSource() {
        val message = factory.parse(IrcMessage(command = "NICK", prefix = "someone@somewhere", parameters = listOf("someone-else")))

        assertEquals(NickMessage(source = Prefix(nick = "someone", host = "somewhere"), nickname = "someone-else"), message)
    }

    @Test fun test_parse_noParameters() {
        val message = factory.parse(IrcMessage(command = "NICK"))

        assertNull(message)
    }

    @Test fun test_parse_invalidHopcount() {
        val message = factory.parse(IrcMessage(command = "NICK", parameters = listOf("nickname", "not_a_number")))

        assertNull(message)
    }

    @Test fun test_serialise_nicknameOnly() {
        val message = factory.serialise(NickMessage(nickname = "nickname"))

        assertEquals(message, IrcMessage(command = "NICK", parameters = listOf("nickname")))
    }

    @Test fun test_serialise_nicknameAndHopcount() {
        val message = factory.serialise(NickMessage(nickname = "nickname", hopcount = 3))

        assertEquals(message, IrcMessage(command = "NICK", parameters = listOf("nickname", "3")))
    }

    @Test fun test_Serialise_WithSource() {
        val message = factory.serialise(NickMessage(source = Prefix(nick = "someone", host = "somewhere"), nickname = "someone-else"))

        assertEquals(IrcMessage(command = "NICK", prefix = "someone@somewhere", parameters = listOf("someone-else")), message)
    }
}