package chat.willow.kale.irc.message.extension.sasl.rpl

import chat.willow.kale.generator.RplSourceTargetContent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class Rpl905MessageTests {

    private lateinit var sut: Rpl905Message

    @Before fun setUp() {
        sut = Rpl905Message
    }

    @Test fun test_command_correct() {
        assertEquals("905", Rpl905Message.command)
    }

    @Test fun test_parser_correct_instance() {
        assertTrue(Rpl905Message.Parser is RplSourceTargetContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        assertTrue(Rpl905Message.Serialiser is RplSourceTargetContent.Serialiser)
    }
    
}