package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl003MessageTests {

    private lateinit var sut: Rpl003Message

    @Before fun setUp() {
        sut = Rpl003Message
    }

    @Test fun test_command_correct() {
        assertEquals("003", Rpl003Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl003Message.Parser is RplSourceTargetContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl003Message.Serialiser is RplSourceTargetContent.Serialiser)
    }

}