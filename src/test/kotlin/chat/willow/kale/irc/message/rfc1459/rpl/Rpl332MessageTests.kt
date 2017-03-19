package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl332MessageTests {

    private lateinit var sut: Rpl332Message

    @Before fun setUp() {
        sut = Rpl332Message
    }

    @Test fun test_command_correct() {
        assertEquals("332", Rpl332Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl332Message.Parser is RplSourceTargetChannelContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl332Message.Serialiser is RplSourceTargetChannelContent.Serialiser)
    }
    
}