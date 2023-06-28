package chat.willow.kale.core

import chat.willow.kale.core.tag.Tag
import chat.willow.kale.core.tag.extension.AccountTag
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AccountTagTests {

    private lateinit var sut: AccountTag.Factory

    @BeforeTest
    fun setUp() {
        sut = AccountTag
    }

    @Test
    fun test_parse_SanityCheck() {
        val rawTag = Tag(name = "account", value = "someone")

        val tag = sut.parse(rawTag)

        assertEquals(AccountTag(account = "someone"), tag)
    }

    @Test
    fun test_parse_MissingValue_ReturnsNull() {
        val rawTag = Tag(name = "account", value = null)

        val tag = sut.parse(rawTag)

        assertNull(tag)
    }

    @Test
    fun test_serialise_SanityCheck() {
        val tag = AccountTag(account = "someone")

        val rawTag = sut.serialise(tag)

        assertEquals(Tag(name = "account", value = "someone"), rawTag)
    }

}