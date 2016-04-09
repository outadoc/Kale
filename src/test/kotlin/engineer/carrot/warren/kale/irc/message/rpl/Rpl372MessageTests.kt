package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class Rpl372MessageTests {
    lateinit var factory: IMessageFactory<Rpl372Message>

    @Before fun setUp() {
        factory = Rpl372Message.Factory
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "372", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "- MOTD text")))

        assertEquals(Rpl372Message(source = "imaginary.bunnies.io", target = "test-nickname", contents = "- MOTD text"), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "372", parameters = listOf("test-nickname2", "- MOTD text")))

        assertEquals(Rpl372Message(source = "", target = "test-nickname2", contents = "- MOTD text"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "372", parameters = listOf("test-nickname3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl372Message(source = "", target = "test-nickname2", contents = "- MOTD text"))

        assertEquals(IrcMessage(command = "372", prefix = "", parameters = listOf("test-nickname2", "- MOTD text")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl372Message(source = "", target = "test-nickname2", contents = "- MOTD text"))

        assertEquals(IrcMessage(command = "372", prefix = "", parameters = listOf("test-nickname2", "- MOTD text")), message)
    }
}