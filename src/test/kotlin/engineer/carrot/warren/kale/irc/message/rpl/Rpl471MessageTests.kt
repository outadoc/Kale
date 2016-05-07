package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class Rpl471MessageTests {
    lateinit var factory: IMessageFactory<Rpl471Message>

    @Before fun setUp() {
        factory = Rpl471Message.Factory
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "471", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "#channel", "Cannot join channel (+l)")))

        assertEquals(Rpl471Message(source = "imaginary.bunnies.io", target = "test-nickname", channel = "#channel", contents = "Cannot join channel (+l)"), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "471", parameters = listOf("test-nickname2", "#channel", "Cannot join channel (+l)")))

        assertEquals(Rpl471Message(source = "", target = "test-nickname2", channel = "#channel", contents = "Cannot join channel (+l)"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "471", parameters = listOf("test-nickname3")))
        val messageTwo = factory.parse(IrcMessage(command = "471", parameters = listOf("test-nickname3", "#channel")))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl471Message(source = "", target = "test-nickname2", channel = "#channel", contents = "Cannot join channel (+l)"))

        assertEquals(IrcMessage(command = "471", prefix = "", parameters = listOf("test-nickname2", "#channel", "Cannot join channel (+l)")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl471Message(source = "", target = "test-nickname2", channel = "#channel", contents = "Cannot join channel (+l)"))

        assertEquals(IrcMessage(command = "471", prefix = "", parameters = listOf("test-nickname2", "#channel", "Cannot join channel (+l)")), message)
    }
}