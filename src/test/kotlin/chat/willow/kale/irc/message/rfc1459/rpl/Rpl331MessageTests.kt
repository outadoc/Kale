package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl331MessageTests {

    private lateinit var sut: Rpl331Message

    @Before fun setUp() {
        sut = Rpl331Message
    }

    @Test fun test_command_correct() {
        assertEquals("331", Rpl331Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl331Message.Parser is RplSourceTargetContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl331Message.Serialiser is RplSourceTargetContent.Serialiser)
    }
    
}