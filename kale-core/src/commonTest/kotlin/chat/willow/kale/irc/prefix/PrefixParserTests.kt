package chat.willow.kale.irc.prefix

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class PrefixParserTests {
    lateinit var prefixParser: IPrefixParser

    @BeforeTest fun setUp() {
        prefixParser = PrefixParser
    }

    @Test fun test_parse_MissingNick() {
        val prefixOne = prefixParser.parse("")
        val prefixTwo = prefixParser.parse("!prefix")
        val prefixThree = prefixParser.parse("@host")
        val prefixFour = prefixParser.parse("!prefix@host")
        val prefixFive = prefixParser.parse("!@host")
        val prefixSix = prefixParser.parse("!prefix@")

        assertNull(prefixOne)
        assertNull(prefixTwo)
        assertNull(prefixThree)
        assertNull(prefixFour)
        assertNull(prefixFive)
        assertNull(prefixSix)
    }

    @Test fun test_parse_NoHost() {
        val prefixOne = prefixParser.parse("nick")
        val prefixTwo = prefixParser.parse("nick!prefix")

        assertEquals(Prefix(nick = "nick"), prefixOne)
        assertEquals(Prefix(nick = "nick", user = "prefix"), prefixTwo)
    }

    @Test fun test_parse_WellFormed() {
        val prefixOne = prefixParser.parse("nick@host")
        val prefixTwo = prefixParser.parse("nick!prefix@host")

        assertEquals(Prefix(nick = "nick", host = "host"), prefixOne)
        assertEquals(Prefix(nick = "nick", user = "prefix", host = "host"), prefixTwo)
    }

    @Test fun test_parse_BlankNonNickFields() {
        val prefixOne = prefixParser.parse("nick!")
        val prefixTwo = prefixParser.parse("nick@")
        val prefixThree = prefixParser.parse("nick!@")

        assertEquals(Prefix(nick = "nick", user = ""), prefixOne)
        assertEquals(Prefix(nick = "nick", host = ""), prefixTwo)
        assertEquals(Prefix(nick = "nick", user = "", host = ""), prefixThree)
    }

    @Test fun test_parse_WeirdExamples() {
        val prefixOne = prefixParser.parse("nick!prefix!resu@host")
        val prefixTwo = prefixParser.parse("nick@kcin!prefix@host")

        assertEquals(Prefix(nick = "nick", user = "prefix!resu", host = "host"), prefixOne)
        assertEquals(Prefix(nick = "nick@kcin", user = "prefix", host = "host"), prefixTwo)
    }
}