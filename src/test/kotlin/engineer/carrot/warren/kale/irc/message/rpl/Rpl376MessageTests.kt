package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl376MessageTests {
    lateinit var factory: Rpl376Message.Factory

    @Before fun setUp() {
        factory = Rpl376Message.Factory
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "376", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "End of MOTD command")))

        assertEquals(Rpl376Message(source = "imaginary.bunnies.io", target = "test-nickname", contents = "End of MOTD command"), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "376", parameters = listOf("test-nickname2", "End of MOTD command")))

        assertEquals(Rpl376Message(source = "", target = "test-nickname2", contents = "End of MOTD command"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "376", parameters = listOf("test-nickname3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl376Message(source = "", target = "test-nickname2", contents = "End of MOTD command"))

        assertEquals(IrcMessage(command = "376", prefix = "", parameters = listOf("test-nickname2", "End of MOTD command")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl376Message(source = "", target = "test-nickname2", contents = "End of MOTD command"))

        assertEquals(IrcMessage(command = "376", prefix = "", parameters = listOf("test-nickname2", "End of MOTD command")), message)
    }
}