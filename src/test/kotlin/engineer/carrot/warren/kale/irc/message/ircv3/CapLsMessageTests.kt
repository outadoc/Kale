package engineer.carrot.warren.kale.irc.message.ircv3

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class CapLsMessageTests {
    lateinit var factory: IMessageFactory<CapLsMessage>

    @Before fun setUp() {
        factory = CapLsMessage.Factory
    }

    @Test fun test_parse_SingleCap() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "LS", "cap1 ")))

        assertEquals(CapLsMessage(target = "test-nick", caps = mapOf("cap1" to null)), message)
    }

    @Test fun test_parse_MultipleCaps() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "LS", "cap1 cap2=value cap3=")))

        assertEquals(CapLsMessage(target = "test-nick", caps = mapOf("cap1" to null, "cap2" to "value", "cap3" to "")), message)
    }

    @Test fun test_parse_MultilineCaps() {
        val message = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "LS", "*", "cap1 cap2=value cap3=")))

        assertEquals(CapLsMessage(target = "test-nick", caps = mapOf("cap1" to null, "cap2" to "value", "cap3" to ""), isMultiline = true), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "CAP", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick")))
        val messageThree = factory.parse(IrcMessage(command = "CAP", parameters = listOf("test-nick", "LS")))

        assertNull(messageOne)
        assertNull(messageTwo)
        assertNull(messageThree)
    }

    @Test fun test_serialise_SingleCap() {
        val message = factory.serialise(CapLsMessage(caps = mapOf("cap1" to null)))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("LS", "302", "cap1")), message)
    }

    @Test fun test_serialise_MultipleCaps() {
        val message = factory.serialise(CapLsMessage(caps = mapOf("cap1" to null, "cap2" to "", "cap3" to "val3")))

        assertEquals(IrcMessage(command = "CAP", parameters = listOf("LS", "302", "cap1 cap2= cap3=val3")), message)
    }

}