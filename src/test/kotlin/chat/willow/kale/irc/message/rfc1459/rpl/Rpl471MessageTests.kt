package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl471MessageTests {

    private lateinit var sut: Rpl471Message

    @Before fun setUp() {
        sut = Rpl471Message
    }

    @Test fun test_command_correct() {
        assertEquals("471", Rpl471Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl471Message.Parser is RplSourceTargetChannelContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl471Message.Serialiser is RplSourceTargetChannelContent.Serialiser)
    }
    
}