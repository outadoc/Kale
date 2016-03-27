package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class Rpl001MessageTests {
    lateinit var factory: IMessageFactory<Rpl001Message>

    @Before fun setUp() {
        factory = Rpl001Message.Factory
    }

    @Test fun test_parse_SourceTargetContents() {
        val message = factory.parse(IrcMessage(command = "001", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "Welcome to the network!")))

        assertEquals(Rpl001Message(source = "imaginary.bunnies.io", target = "test-nickname", contents = "Welcome to the network!"), message)
    }

    @Test fun test_parse_TargetContents_SourceIsEmptyString() {
        val message = factory.parse(IrcMessage(command = "001", parameters = listOf("test-nickname2", "Welcome to the network!")))

        assertEquals(Rpl001Message(source = "", target = "test-nickname2", contents = "Welcome to the network!"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val message = factory.parse(IrcMessage(command = "001", parameters = listOf("test-nickname3")))

        assertNull(message)
    }

    @Test fun test_serialise_SourceTargetContents() {
        val message = factory.serialise(Rpl001Message(source = "", target = "test-nickname2", contents = "Welcome to the network!"))

        assertEquals(IrcMessage(command = "001", prefix = "", parameters = listOf("test-nickname2", "Welcome to the network!")), message)
    }

    @Test fun test_serialise_TargetContents_SourceIsEmptyString() {
        val message = factory.serialise(Rpl001Message(source = "", target = "test-nickname2", contents = "Welcome to the network!"))

        assertEquals(IrcMessage(command = "001", prefix = "", parameters = listOf("test-nickname2", "Welcome to the network!")), message)
    }
}