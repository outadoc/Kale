package chat.willow.kale.core

import chat.willow.kale.core.tag.ITagParser
import chat.willow.kale.core.tag.ITagSerialiser
import chat.willow.kale.core.tag.KaleTagRouter
import org.kodein.mock.Mock
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertSame

class KaleTagRouterTests : TestsWithMocks() {
    override fun setUpMocks() = injectMocks(mocker)

    private lateinit var sut: KaleTagRouter

    @Mock
    lateinit var mockTagParser: ITagParser<Any>

    @Mock
    lateinit var mockTagSerialiser: ITagSerialiser<Any>

    private val mockTag = Any()

    @BeforeTest
    fun setUp() {
        sut = KaleTagRouter()
    }

    @Test
    fun test_parserFor_AfterRegisteringTagParser_ReturnsCorrectParser() {
        sut.routeTagToParser("test", mockTagParser)

        val parser = sut.parserFor("test")

        assertSame(mockTagParser, parser)
    }

    @Test
    fun test_parserFor_WrongName_ReturnsNull() {
        sut.routeTagToParser("test", mockTagParser)

        val parser = sut.parserFor("different test")

        assertNull(parser)
    }

    @Test
    fun test_serialiserFor_AfterRegisteringTagSerialiser_ReturnsCorrectSerialiser() {
        sut.routeTagToSerialiser(mockTag::class, mockTagSerialiser)

        val serialiser = sut.serialiserFor(mockTag::class)

        assertSame(mockTagSerialiser, serialiser)
    }

    @Test
    fun test_serialiserFor_WrongType_ReturnsNull() {
        sut.routeTagToSerialiser(mockTag::class, mockTagSerialiser)

        val serialiser = sut.serialiserFor(Int::class)

        assertNull(serialiser)
    }

}