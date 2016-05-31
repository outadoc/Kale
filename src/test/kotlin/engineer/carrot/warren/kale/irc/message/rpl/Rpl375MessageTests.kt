package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl375MessageTests {
    lateinit var factory: Rpl375Message.Factory

    @Before fun setUp() {
        factory = Rpl375Message.Factory
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "375", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "- imaginary.bunnies.io Message of the day - ")))

        assertEquals(Rpl375Message(source = "imaginary.bunnies.io", target = "test-nickname", contents = "- imaginary.bunnies.io Message of the day - "), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "375", parameters = listOf("test-nickname2", "- imaginary.bunnies.io Message of the day - ")))

        assertEquals(Rpl375Message(source = "", target = "test-nickname2", contents = "- imaginary.bunnies.io Message of the day - "), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "375", parameters = listOf("test-nickname3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl375Message(source = "", target = "test-nickname2", contents = "- imaginary.bunnies.io Message of the day - "))

        assertEquals(IrcMessage(command = "375", prefix = "", parameters = listOf("test-nickname2", "- imaginary.bunnies.io Message of the day - ")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl375Message(source = "", target = "test-nickname2", contents = "- imaginary.bunnies.io Message of the day - "))

        assertEquals(IrcMessage(command = "375", prefix = "", parameters = listOf("test-nickname2", "- imaginary.bunnies.io Message of the day - ")), message)
    }
}