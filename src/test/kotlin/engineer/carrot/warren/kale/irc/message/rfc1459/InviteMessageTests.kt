package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class InviteMessageTests {
    lateinit var factory: IMessageFactory<InviteMessage>

    @Before fun setUp() {
        factory = InviteMessage.Factory
    }

    @Test fun test_parse_Source_User_Channel() {
        val message = factory.parse(IrcMessage(command = "INVITE", prefix = "someone", parameters = listOf("nickname", "#channel")))

        assertEquals(InviteMessage(source = "someone", user = "nickname", channel = "#channel"), message)
    }

    @Test fun test_parse_User_Channel_NoSource() {
        val message = factory.parse(IrcMessage(command = "INVITE", parameters = listOf("nickname", "#channel")))

        assertEquals(InviteMessage(user = "nickname", channel = "#channel"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "INVITE", parameters = listOf("nickname")))
        val messageTwo = factory.parse(IrcMessage(command = "INVITE", parameters = listOf()))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_Source_User_Channel() {
        val message = factory.serialise(InviteMessage(source = "source", user = "user", channel = "channel"))

        assertEquals(IrcMessage(command = "INVITE", prefix = "source", parameters = listOf("user", "channel")), message)
    }

    @Test fun test_serialise_User_Channel_NoSource() {
        val message = factory.serialise(InviteMessage(user = "user", channel = "channel"))

        assertEquals(IrcMessage(command = "INVITE", parameters = listOf("user", "channel")), message)
    }

}