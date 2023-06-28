package chat.willow.kale

import chat.willow.kale.core.message.IrcMessage
import chat.willow.kale.core.tag.IKaleTagRouter
import chat.willow.kale.core.tag.ITagParser
import org.kodein.mock.Mock
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class KaleMetadataFactoryTests : TestsWithMocks() {
    override fun setUpMocks() = injectMocks(mocker)

    @Mock
    lateinit var tagRouter: IKaleTagRouter

    @Mock
    lateinit var tagParser: ITagParser<String>

    private val sut: KaleMetadataFactory by withMocks { KaleMetadataFactory(tagRouter) }

    @Test
    fun test_process_ValidMetadata_MetadataIsCorrect() {
        val metadata = "some metadata"

        every { tagParser.parse(isAny()) } returns metadata
        every { tagRouter.parserFor("tag1") } returns tagParser
        every { tagRouter.parserFor("tag2") } returns tagParser

        val result = sut.construct(IrcMessage(tags = mapOf("tag1" to "value1", "tag2" to null), command = ""))

        assertEquals(metadata, result[String::class])
    }

    @Test
    fun test_process_InvalidMetadataLookup_MetadataIsNull() {
        val metadata = "some metadata"

        every { tagParser.parse(isAny()) } returns metadata
        every { tagRouter.parserFor("tag1") } returns tagParser
        every { tagRouter.parserFor("tag2") } returns tagParser

        val result = sut.construct(IrcMessage(tags = mapOf("tag1" to "value1", "tag2" to null), command = ""))

        assertNull(result[Int::class])
    }

}