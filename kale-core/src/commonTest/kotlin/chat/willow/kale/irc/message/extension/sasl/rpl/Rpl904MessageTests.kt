package chat.willow.kale.irc.message.extension.sasl.rpl

import chat.willow.kale.core.RplSourceTargetContent
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test

class Rpl904MessageTests {
    
    private lateinit var sut: Rpl904Message

    @BeforeTest fun setUp() {
        sut = Rpl904Message
    }

    @Test fun test_command_correct() {
        assertEquals("904", Rpl904Message.command)
    }

    @Test fun test_parser_correct_instance() {
        assertTrue(Rpl904Message.Parser is RplSourceTargetContent.Parser)
    }

    @Test fun test_serialiser_correct_instance() {
        assertTrue(Rpl904Message.Serialiser is RplSourceTargetContent.Serialiser)
    }
    
}