package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.prefix.prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class NickMessageTests {

    private lateinit var messageParser: NickMessage.Message.Parser
    private lateinit var messageSerialiser: NickMessage.Command.Serialiser

    @Before fun setUp() {
        messageParser = NickMessage.Message.Parser
        messageSerialiser = NickMessage.Command.Serialiser
    }

    @Test fun test_parse_nicknameOnly() {
        val message = messageParser.parse(IrcMessage(command = "NICK", prefix = "someone", parameters = listOf("test_nickname")))

        assertEquals(message, NickMessage.Message(source = prefix("someone"), nickname = "test_nickname"))
    }

    @Test fun test_parse_WithSource() {
        val message = messageParser.parse(IrcMessage(command = "NICK", prefix = "someone@somewhere", parameters = listOf("someone-else")))

        assertEquals(NickMessage.Message(source = Prefix(nick = "someone", host = "somewhere"), nickname = "someone-else"), message)
    }

    @Test fun test_parse_noParameters() {
        val message = messageParser.parse(IrcMessage(command = "NICK"))

        assertNull(message)
    }

    @Test fun test_parse_invalidHopcount() {
        val message = messageParser.parse(IrcMessage(command = "NICK", parameters = listOf("nickname", "not_a_number")))

        assertNull(message)
    }

    @Test fun test_serialise_nicknameOnly() {
        val message = messageSerialiser.serialise(NickMessage.Command(nickname = "nickname"))

        assertEquals(message, IrcMessage(command = "NICK", parameters = listOf("nickname")))
    }

}