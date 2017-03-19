package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl002MessageTests {

    private lateinit var sut: Rpl002Message

    @Before fun setUp() {
        sut = Rpl002Message
    }

    @Test fun test_command_correct() {
        assertEquals("002", Rpl002Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl002Message.Parser is RplSourceTargetContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl002Message.Serialiser is RplSourceTargetContent.Serialiser)
    }

}