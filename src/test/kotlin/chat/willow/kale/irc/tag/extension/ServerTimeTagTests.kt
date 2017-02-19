package chat.willow.kale.irc.tag.extension

import chat.willow.kale.irc.tag.Tag
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.Instant

class ServerTimeTagTests {

    private lateinit var sut: ServerTimeTag.Factory

    @Before fun setUp() {
        sut = ServerTimeTag.Factory
    }

    @Test fun test_parse_SanityCheck() {
        val rawTag = Tag(name = "time", value = "2017-02-19T20:43:12.345Z")

        val tag = sut.parse(rawTag)

        assertEquals(tag, ServerTimeTag(Instant.ofEpochMilli(1487536992345)))
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
        val tag = ServerTimeTag(time = Instant.ofEpochMilli(1487536992345))

        val rawTag = sut.serialise(tag)

        assertEquals(Tag(name = "time", value = "2017-02-19T20:43:12.345Z"), rawTag)
    }

}