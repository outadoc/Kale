package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl003MessageTests {
    lateinit var factory: Rpl003Message.Factory

    @Before fun setUp() {
        factory = Rpl003Message.Factory
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "003", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "This server was created DATE")))

        assertEquals(Rpl003Message(source = "imaginary.bunnies.io", target = "test-nickname", contents = "This server was created DATE"), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "003", parameters = listOf("test-nickname2", "This server was created DATE")))

        assertEquals(Rpl003Message(source = "", target = "test-nickname2", contents = "This server was created DATE"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "003", parameters = listOf("test-nickname3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl003Message(source = "", target = "test-nickname2", contents = "This server was created DATE"))

        assertEquals(IrcMessage(command = "003", prefix = "", parameters = listOf("test-nickname2", "This server was created DATE")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl003Message(source = "", target = "test-nickname2", contents = "This server was created DATE"))

        assertEquals(IrcMessage(command = "003", prefix = "", parameters = listOf("test-nickname2", "This server was created DATE")), message)
    }
}