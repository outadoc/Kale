package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl475MessageTests {

    private lateinit var sut: Rpl475Message

    @Before fun setUp() {
        sut = Rpl475Message
    }

    @Test fun test_command_correct() {
        assertEquals("475", Rpl475Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl475Message.Parser is RplSourceTargetChannelContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl475Message.Serialiser is RplSourceTargetChannelContent.Serialiser)
    }
    
}