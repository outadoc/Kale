package engineer.carrot.warren.kale.irc.message.ircv3.sasl

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl903MessageTests {
    lateinit var factory: Rpl903Message.Factory

    @Before fun setUp() {
        factory = Rpl903Message
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "903", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "SASL authentication successful")))

        assertEquals(Rpl903Message(source = "imaginary.bunnies.io", target = "test-nickname", contents = "SASL authentication successful"), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "903", parameters = listOf("test-nickname2", "SASL authentication successful")))

        assertEquals(Rpl903Message(source = "", target = "test-nickname2", contents = "SASL authentication successful"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "903", parameters = listOf("test-nickname3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl903Message(source = "", target = "test-nickname2", contents = "SASL authentication successful"))

        assertEquals(IrcMessage(command = "903", prefix = "", parameters = listOf("test-nickname2", "SASL authentication successful")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl903Message(source = "", target = "test-nickname2", contents = "SASL authentication successful"))

        assertEquals(IrcMessage(command = "903", prefix = "", parameters = listOf("test-nickname2", "SASL authentication successful")), message)
    }
}