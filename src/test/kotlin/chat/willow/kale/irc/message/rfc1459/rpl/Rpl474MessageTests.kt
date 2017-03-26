package chat.willow.kale.irc.message.rfc1459.rpl

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rpl474MessageTests {

    private lateinit var sut: Rpl474Message

    @Before fun setUp() {
        sut = Rpl474Message
    }

    @Test fun test_command_correct() {
        assertEquals("474", Rpl474Message.command)
    }

    @Test fun test_parser_correct_instance() {
        Assert.assertTrue(Rpl474Message.Parser is RplSourceTargetChannelContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        Assert.assertTrue(Rpl474Message.Serialiser is RplSourceTargetChannelContent.Serialiser)
    }

}