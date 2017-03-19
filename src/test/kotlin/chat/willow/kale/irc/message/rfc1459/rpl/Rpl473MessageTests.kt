package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl473MessageTests {

    private lateinit var sut: Rpl473Message

    @Before fun setUp() {
        sut = Rpl473Message
    }

    @Test fun test_command_correct() {
        assertEquals("473", Rpl473Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl473Message.Parser is RplSourceTargetChannelContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl473Message.Serialiser is RplSourceTargetChannelContent.Serialiser)
    }
    
}