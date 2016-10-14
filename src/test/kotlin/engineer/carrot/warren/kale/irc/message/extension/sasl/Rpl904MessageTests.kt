package engineer.carrot.warren.kale.irc.message.extension.sasl

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl904MessageTests {
    lateinit var factory: Rpl904Message.Factory

    @Before fun setUp() {
        factory = Rpl904Message
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "904", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "SASL authentication failed")))

        assertEquals(Rpl904Message(source = "imaginary.bunnies.io", target = "test-nickname", contents = "SASL authentication failed"), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "904", parameters = listOf("test-nickname2", "SASL authentication failed")))

        assertEquals(Rpl904Message(source = "", target = "test-nickname2", contents = "SASL authentication failed"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "904", parameters = listOf("test-nickname3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl904Message(source = "", target = "test-nickname2", contents = "SASL authentication failed"))

        assertEquals(IrcMessage(command = "904", prefix = "", parameters = listOf("test-nickname2", "SASL authentication failed")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl904Message(source = "", target = "test-nickname2", contents = "SASL authentication failed"))

        assertEquals(IrcMessage(command = "904", prefix = "", parameters = listOf("test-nickname2", "SASL authentication failed")), message)
    }
}