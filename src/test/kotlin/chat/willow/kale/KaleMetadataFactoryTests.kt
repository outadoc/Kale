package chat.willow.kale

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.tag.IKaleTagRouter
import chat.willow.kale.irc.tag.ITagParser
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class KaleMetadataFactoryTests {

    private lateinit var sut: KaleMetadataFactory
    private lateinit var tagRouter: IKaleTagRouter

    @Before fun setUp() {
        tagRouter = mock()

        sut = KaleMetadataFactory(tagRouter)
    }

    @Test fun test_process_ValidMetadata_MetadataIsCorrect() {
        val tagParser: ITagParser<String> = mock()
        val metadata = "some metadata"
        whenever(tagParser.parse(any())).thenReturn(metadata)
        whenever(tagRouter.parserFor("tag1")).thenReturn(tagParser)

        val result = sut.construct(IrcMessage(tags = mapOf("tag1" to "value1", "tag2" to null), command = ""))

        assertEquals(metadata, result[String::class])
    }

    @Test fun test_process_InvalidMetadataLookup_MetadataIsNull() {
        val tagParser: ITagParser<String> = mock()
        val metadata = "some metadata"
        whenever(tagParser.parse(any())).thenReturn(metadata)
        whenever(tagRouter.parserFor("tag1")).thenReturn(tagParser)

        val result = sut.construct(IrcMessage(tags = mapOf("tag1" to "value1", "tag2" to null), command = ""))

        assertNull(result[Int::class])
    }

}