package chat.willow.kale.core

import chat.willow.kale.core.tag.Tag
import chat.willow.kale.core.tag.extension.ServerTimeTag
import kotlinx.datetime.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class ServerTimeTagTests {

    private lateinit var sut: ServerTimeTag.Factory

    @BeforeTest fun setUp() {
        sut = ServerTimeTag.Factory
    }

    @Test fun test_parse_SanityCheck() {
        val rawTag = Tag(name = "time", value = "2017-02-19T20:43:12.345Z")

        val tag = sut.parse(rawTag)

        assertEquals(tag, ServerTimeTag(Instant.fromEpochMilliseconds(1487536992345)))
    }

    @Test fun test_parse_MissingValue_ReturnsNull() {
        val rawTag = Tag(name = "time", value = null)

        val tag = sut.parse(rawTag)

        assertNull(tag)
    }

    @Test fun test_parse_UnparseableDate_ReturnsNull() {
        val rawTag = Tag(name = "time", value = "not a time")

        val tag = sut.parse(rawTag)

        assertNull(tag)
    }

    @Test fun test_serialise_SanityCheck() {
        val tag = ServerTimeTag(time = Instant.fromEpochMilliseconds(1487536992345))

        val rawTag = sut.serialise(tag)

        assertEquals(Tag(name = "time", value = "2017-02-19T20:43:12.345Z"), rawTag)
    }

}