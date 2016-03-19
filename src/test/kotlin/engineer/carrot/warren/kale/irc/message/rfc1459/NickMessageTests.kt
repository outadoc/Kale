package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class NickMessageTests {
    lateinit var factory: IMessageFactory<NickMessage>

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
}