package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl001MessageTests {

    private lateinit var sut: Rpl001Message

    @Before fun setUp() {
        sut = Rpl001Message
    }

    @Test fun test_command_correct() {
        assertEquals("001", Rpl001Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl001Message.Parser is RplSourceTargetContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl001Message.Serialiser is RplSourceTargetContent.Serialiser)
    }

}