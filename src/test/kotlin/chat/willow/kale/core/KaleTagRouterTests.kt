package chat.willow.kale.core

import chat.willow.kale.core.tag.ITagParser
import chat.willow.kale.core.tag.ITagSerialiser
import chat.willow.kale.core.tag.KaleTagRouter
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class KaleTagRouterTests {

    private lateinit var sut: KaleTagRouter

    @Before fun setUp() {
        sut = KaleTagRouter()
    }

    @Test fun test_parserFor_AfterRegisteringTagParser_ReturnsCorrectParser() {
        val mockTagParser: ITagParser<*> = mock()
        sut.routeTagToParser("test", mockTagParser)

        val parser = sut.parserFor("test")

        assertSame(mockTagParser, parser)
    }

    @Test fun test_parserFor_WrongName_ReturnsNull() {
        val mockTagParser: ITagParser<*> = mock()
        sut.routeTagToParser("test", mockTagParser)

        val parser = sut.parserFor("different test")

        assertNull(parser)
    }

    @Test fun test_serialiserFor_AfterRegisteringTagSerialiser_ReturnsCorrectSerialiser() {
        val mockTagSerialiser: ITagSerialiser<Any> = mock()
        val mockTag: Any = mock()
        sut.routeTagToSerialiser(mockTag::class, mockTagSerialiser)

        val serialiser = sut.serialiserFor(mockTag::class.java)

        assertSame(mockTagSerialiser, serialiser)
    }

    @Test fun test_serialiserFor_WrongType_ReturnsNull() {
        val mockTagSerialiser: ITagSerialiser<Any> = mock()
        val mockTag: Any = mock()
        sut.routeTagToSerialiser(mockTag::class, mockTagSerialiser)

        val serialiser = sut.serialiserFor(Int::class.java)

        assertNull(serialiser)
    }

}