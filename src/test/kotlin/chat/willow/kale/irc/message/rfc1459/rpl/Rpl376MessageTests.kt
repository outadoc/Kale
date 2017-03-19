package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl376MessageTests {

    private lateinit var sut: Rpl376Message

    @Before fun setUp() {
        sut = Rpl376Message
    }

    @Test fun test_command_correct() {
        assertEquals("376", Rpl376Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl376Message.Parser is RplSourceTargetContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl376Message.Serialiser is RplSourceTargetContent.Serialiser)
    }
    
}