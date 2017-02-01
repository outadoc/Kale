package chat.willow.kale.irc.message.extension.monitor.rpl

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.prefix.Prefix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RplMonOnlineMessageTests {
    private lateinit var factory: RplMonOnlineMessage.Factory

    @Before fun setUp() {
        factory = RplMonOnlineMessage
    }

    @Test fun test_parse_ValidPrefix_SingleTarget() {
        val message = factory.parse(IrcMessage(command = "730", prefix = "server", parameters = listOf("*", "someone!user@somewhere")))

        assertEquals(RplMonOnlineMessage(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone", user = "user", host = "somewhere"))), message)
    }

    @Test fun test_parse_ValidPrefix_MultipleTargets() {
        val message = factory.parse(IrcMessage(command = "730", prefix = "server", parameters = listOf("*", "someone!user@somewhere,someone-else")))

        assertEquals(RplMonOnlineMessage(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone", user = "user", host = "somewhere"), Prefix(nick = "someone-else"))), message)
    }

    @Test fun test_parse_MissingPrefix() {
        val message = factory.parse(IrcMessage(command = "730", prefix = null, parameters = listOf("*", "someone!user@somewhere,someone-else")))

        assertNull(message)
    }

    @Test fun test_parse_InvalidPrefix() {
        val message = factory.parse(IrcMessage(command = "730", prefix = "!!!", parameters = listOf("*", "someone!user@somewhere,someone-else")))

        assertNull(message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "730", prefix = "server", parameters = listOf("*")))
        val messageTwo = factory.parse(IrcMessage(command = "730", prefix = "server", parameters = listOf()))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_SingleTarget() {
        val message = factory.serialise(RplMonOnlineMessage(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone"))))

        assertEquals(IrcMessage(command = "730", prefix = "server", parameters = listOf("*", "someone")), message)
    }

    @Test fun test_serialise_MultipleTargets() {
        val message = factory.serialise(RplMonOnlineMessage(prefix = Prefix(nick = "server"), nickOrStar = "*", targets = listOf(Prefix(nick = "someone"), Prefix(nick = "someone-else", user = "user", host = "somewhere"))))

        assertEquals(IrcMessage(command = "730", prefix = "server", parameters = listOf("*", "someone,someone-else!user@somewhere")), message)
    }

}