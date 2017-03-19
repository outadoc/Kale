package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl422MessageTests {

    private lateinit var sut: Rpl422Message

    @Before fun setUp() {
        sut = Rpl422Message
    }

    @Test fun test_command_correct() {
        assertEquals("422", Rpl422Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl422Message.Parser is RplSourceTargetContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl422Message.Serialiser is RplSourceTargetContent.Serialiser)
    }
    
}