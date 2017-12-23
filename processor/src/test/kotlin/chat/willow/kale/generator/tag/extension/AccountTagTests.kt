package chat.willow.kale.generator.tag.extension

import chat.willow.kale.generator.tag.Tag
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AccountTagTests {

    private lateinit var sut: AccountTag.Factory

    @Before fun setUp() {
        sut = AccountTag
    }

    @Test fun test_parse_SanityCheck() {
        val rawTag = Tag(name = "account", value = "someone")

        val tag = sut.parse(rawTag)

        assertEquals(AccountTag(account = "someone"), tag)
    }

    @Test fun test_parse_MissingValue_ReturnsNull() {
        val rawTag = Tag(name = "account", value = null)

        val tag = sut.parse(rawTag)

        assertNull(tag)
    }

    @Test fun test_serialise_SanityCheck() {
        val tag = AccountTag(account = "someone")

        val rawTag = sut.serialise(tag)

        assertEquals(Tag(name = "account", value = "someone"), rawTag)
    }

}