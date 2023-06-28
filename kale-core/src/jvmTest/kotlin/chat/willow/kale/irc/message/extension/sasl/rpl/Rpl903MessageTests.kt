package chat.willow.kale.irc.message.extension.sasl.rpl

import chat.willow.kale.core.RplSourceTargetContent
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test

class Rpl903MessageTests {

    private lateinit var sut: Rpl903Message

    @BeforeTest fun setUp() {
        sut = Rpl903Message
    }

    @Test fun test_command_correct() {
        assertEquals("903", Rpl903Message.command)
    }

    @Test fun test_parser_correct_instance() {
        assertTrue(Rpl903Message.Parser is RplSourceTargetContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        assertTrue(Rpl903Message.Serialiser is RplSourceTargetContent.Serialiser)
    }

}