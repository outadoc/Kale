package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class InviteMessageTests {
    lateinit var factory: InviteMessage.Factory

    @Before fun setUp() {
        factory = InviteMessage.Factory
    }

    @Test fun test_parse_Source_User_Channel() {
        val message = factory.parse(IrcMessage(command = "INVITE", prefix = "someone", parameters = listOf("nickname", "#channel")))

        assertEquals(InviteMessage(source = Prefix(nick = "someone"), user = "nickname", channel = "#channel"), message)
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
        val message = factory.serialise(InviteMessage(source = Prefix(nick = "source"), user = "user", channel = "channel"))

        assertEquals(IrcMessage(command = "INVITE", prefix = "source", parameters = listOf("user", "channel")), message)
    }

    @Test fun test_serialise_User_Channel_NoSource() {
        val message = factory.serialise(InviteMessage(user = "user", channel = "channel"))

        assertEquals(IrcMessage(command = "INVITE", parameters = listOf("user", "channel")), message)
    }

}