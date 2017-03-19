package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl372MessageTests {

    private lateinit var sut: Rpl372Message

    @Before fun setUp() {
        sut = Rpl372Message
    }

    @Test fun test_command_correct() {
        assertEquals("372", Rpl372Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl372Message.Parser is RplSourceTargetContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl372Message.Serialiser is RplSourceTargetContent.Serialiser)
    }
    
}