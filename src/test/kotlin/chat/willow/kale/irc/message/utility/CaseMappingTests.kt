package chat.willow.kale.irc.message.utility

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CaseMappingTests {

    val stringWithAllCaseMappingValues = "ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^"
    val expectedAsciiLowerCase = "abcdefghijklmnopqrstuvwxyz[]^"
    val expectedStrictRFC1459LowerCase = "abcdefghijklmnopqrstuvwxyz{}^"
    val expectedRFC1459LowerCase = "abcdefghijklmnopqrstuvwxyz{}~"

    @Test fun test_toLower_ASCII() {
        assertEquals(expectedAsciiLowerCase, CaseMapping.ASCII.toLower(stringWithAllCaseMappingValues))
    }

    @Test fun test_toLower_STRICT_RFC1459() {
        assertEquals(expectedStrictRFC1459LowerCase, CaseMapping.STRICT_RFC1459.toLower(stringWithAllCaseMappingValues))
    }

    @Test fun test_toLower_RFC1459() {
        assertEquals(expectedRFC1459LowerCase, CaseMapping.RFC1459.toLower(stringWithAllCaseMappingValues))
    }

    @Test fun test_equalsIgnoreCase_ASCII() {
        assertTrue(equalsIgnoreCase(CaseMapping.ASCII, stringWithAllCaseMappingValues, expectedAsciiLowerCase))
    }

    @Test fun test_equalsIgnoreCase_STRICT_RFC1459() {
        assertTrue(equalsIgnoreCase(CaseMapping.STRICT_RFC1459, stringWithAllCaseMappingValues, expectedStrictRFC1459LowerCase))
    }

    @Test fun test_equalsIgnoreCase_RFC1459() {
        assertTrue(equalsIgnoreCase(CaseMapping.RFC1459, stringWithAllCaseMappingValues, expectedRFC1459LowerCase))
    }

}
