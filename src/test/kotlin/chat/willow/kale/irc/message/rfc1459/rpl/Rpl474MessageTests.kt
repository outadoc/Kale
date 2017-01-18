package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl474MessageTests {
    lateinit var factory: Rpl474Message.Factory

    @Before fun setUp() {
        factory = Rpl474Message
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "474", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "#channel", "Cannot join channel (+b)")))

        assertEquals(Rpl474Message(source = "imaginary.bunnies.io", target = "test-nickname", channel = "#channel", contents = "Cannot join channel (+b)"), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "474", parameters = listOf("test-nickname2", "#channel", "Cannot join channel (+b)")))

        assertEquals(Rpl474Message(source = "", target = "test-nickname2", channel = "#channel", contents = "Cannot join channel (+b)"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "474", parameters = listOf("test-nickname3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl474Message(source = "", target = "test-nickname2", channel = "#channel", contents = "Cannot join channel (+b)"))

        assertEquals(IrcMessage(command = "474", prefix = "", parameters = listOf("test-nickname2", "#channel", "Cannot join channel (+b)")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl474Message(source = "", target = "test-nickname2", channel = "#channel", contents = "Cannot join channel (+b)"))

        assertEquals(IrcMessage(command = "474", prefix = "", parameters = listOf("test-nickname2", "#channel", "Cannot join channel (+b)")), message)
    }
}