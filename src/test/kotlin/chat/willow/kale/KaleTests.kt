package chat.willow.kale

import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.tag.IKaleTagRouter
import chat.willow.kale.irc.tag.ITagParser
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor

class KaleTests {

    private lateinit var sut: Kale
    private lateinit var router: IKaleRouter
    private lateinit var tagRouter: IKaleTagRouter
    private lateinit var metadata: IMetadataStore

    private lateinit var handlerOne: IKaleIrcMessageHandler
    private lateinit var handlerTwo: IKaleIrcMessageHandler

    @Before fun setUp() {
        router = mock()
        tagRouter = mock()
        metadata = mock()

        handlerOne = mock()
        handlerTwo = mock()

        sut = Kale(router, tagRouter)
    }

    @Test fun test_process_UnparseableLine_DoesNothing() {
        sut.process(" ")

        verifyZeroInteractions(router)
        verifyZeroInteractions(tagRouter)
    }

    @Test fun test_process_NoHandler_DoesNothing() {
        whenever(router.handlerFor("1")).thenReturn(handlerOne)
        whenever(router.handlerFor("2")).thenReturn(handlerTwo)

        sut.process("COMMAND")

        verify(router, only()).handlerFor("COMMAND")
        verifyZeroInteractions(handlerOne)
        verifyZeroInteractions(handlerTwo)
    }

    @Test fun test_process_TellsHandlerToHandle() {
        whenever(router.handlerFor("1")).thenReturn(handlerOne)
        whenever(router.handlerFor("2")).thenReturn(handlerTwo)

        sut.process("1")

        verify(handlerOne).handle(eq(IrcMessage(command = "1")), any())
        verifyZeroInteractions(handlerTwo)
    }

    @Test fun test_process_HandlesInOrder() {
        whenever(router.handlerFor("1")).thenReturn(handlerOne)
        whenever(router.handlerFor("2")).thenReturn(handlerTwo)

        sut.process("1")
        sut.process("2")
        sut.process("3")

        inOrder(handlerOne, handlerTwo) {
            verify(handlerOne).handle(eq(IrcMessage(command = "1")), any())
            verify(handlerTwo).handle(eq(IrcMessage(command = "2")), any())
        }
    }

    @Test fun test_process_ValidMetadata_MetadataIsCorrect() {
        whenever(router.handlerFor("1")).thenReturn(handlerOne)

        val tagParser: ITagParser<String> = mock()
        val metadata = "some metadata"
        whenever(tagParser.parse(any())).thenReturn(metadata)
        whenever(tagRouter.parserFor("tag1")).thenReturn(tagParser)

        sut.process("@tag1=value1;tag2 1")

        argumentCaptor<IMetadataStore>().apply {
            verify(handlerOne).handle(any(), capture())

            assertEquals(metadata, firstValue[String::class])
        }
    }

    @Test fun test_process_InvalidMetadataLookup_MetadataIsNull() {
        whenever(router.handlerFor("1")).thenReturn(handlerOne)

        val tagParser: ITagParser<String> = mock()
        val metadata = "some metadata"
        whenever(tagParser.parse(any())).thenReturn(metadata)
        whenever(tagRouter.parserFor("tag1")).thenReturn(tagParser)

        sut.process("@tag1=value1;tag2 1")

        argumentCaptor<IMetadataStore>().apply {
            verify(handlerOne).handle(any(), capture())

            assertNull(firstValue[Int::class])
        }
    }

    @Test fun test_serialise_RouterHasNoSerialiser_ReturnsNull() {
        val thing: Any = mock()

        val result = sut.serialise(thing)

        assertNull(result)
    }

    @Test fun test_serialise_RouterHasSerialiser_ReturnsSerialisedMessage() {
        val thing: Int = 1
        val expectedReturnMessage: IrcMessage = IrcMessage(command = "ANY")

        val anySerialiser = object : IMessageSerialiser<Int> {
            override fun serialise(message: Int): IrcMessage? {
                return expectedReturnMessage
            }
        }

        whenever(router.serialiserFor(any<Class<*>>())).thenReturn(anySerialiser)

        val result = sut.serialise(thing)

        assertTrue(expectedReturnMessage === result)
    }

}