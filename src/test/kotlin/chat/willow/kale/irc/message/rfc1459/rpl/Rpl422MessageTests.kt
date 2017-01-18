package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl422MessageTests {
    lateinit var factory: Rpl422Message.Factory

    @Before fun setUp() {
        factory = Rpl422Message
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "422", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "MOTD File is missing")))

        assertEquals(Rpl422Message(source = "imaginary.bunnies.io", target = "test-nickname", contents = "MOTD File is missing"), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "422", parameters = listOf("test-nickname2", "MOTD File is missing")))

        assertEquals(Rpl422Message(source = "", target = "test-nickname2", contents = "MOTD File is missing"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "422", parameters = listOf("test-nickname3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl422Message(source = "", target = "test-nickname2", contents = "MOTD File is missing"))

        assertEquals(IrcMessage(command = "422", prefix = "", parameters = listOf("test-nickname2", "MOTD File is missing")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl422Message(source = "", target = "test-nickname2", contents = "MOTD File is missing"))

        assertEquals(IrcMessage(command = "422", prefix = "", parameters = listOf("test-nickname2", "MOTD File is missing")), message)
    }
}