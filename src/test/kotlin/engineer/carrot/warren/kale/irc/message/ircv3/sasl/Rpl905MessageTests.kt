package engineer.carrot.warren.kale.irc.message.ircv3.sasl

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl905MessageTests {
    lateinit var factory: Rpl905Message.Factory

    @Before fun setUp() {
        factory = Rpl905Message
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "905", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "SASL message too long")))

        assertEquals(Rpl905Message(source = "imaginary.bunnies.io", target = "test-nickname", contents = "SASL message too long"), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "905", parameters = listOf("test-nickname2", "SASL message too long")))

        assertEquals(Rpl905Message(source = "", target = "test-nickname2", contents = "SASL message too long"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "905", parameters = listOf("test-nickname3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl905Message(source = "", target = "test-nickname2", contents = "SASL message too long"))

        assertEquals(IrcMessage(command = "905", prefix = "", parameters = listOf("test-nickname2", "SASL message too long")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl905Message(source = "", target = "test-nickname2", contents = "SASL message too long"))

        assertEquals(IrcMessage(command = "905", prefix = "", parameters = listOf("test-nickname2", "SASL message too long")), message)
    }
}