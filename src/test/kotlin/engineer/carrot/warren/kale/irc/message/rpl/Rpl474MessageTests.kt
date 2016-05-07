package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class Rpl474MessageTests {
    lateinit var factory: IMessageFactory<Rpl474Message>

    @Before fun setUp() {
        factory = Rpl474Message.Factory
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "474", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "Cannot join channel (+b)")))

        assertEquals(Rpl474Message(source = "imaginary.bunnies.io", target = "test-nickname", contents = "Cannot join channel (+b)"), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "474", parameters = listOf("test-nickname2", "Cannot join channel (+b)")))

        assertEquals(Rpl474Message(source = "", target = "test-nickname2", contents = "Cannot join channel (+b)"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "474", parameters = listOf("test-nickname3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl474Message(source = "", target = "test-nickname2", contents = "Cannot join channel (+b)"))

        assertEquals(IrcMessage(command = "474", prefix = "", parameters = listOf("test-nickname2", "Cannot join channel (+b)")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl474Message(source = "", target = "test-nickname2", contents = "Cannot join channel (+b)"))

        assertEquals(IrcMessage(command = "474", prefix = "", parameters = listOf("test-nickname2", "Cannot join channel (+b)")), message)
    }
}