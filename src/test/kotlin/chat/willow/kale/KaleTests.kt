package chat.willow.kale

import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.tag.IKaleTagRouter
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KaleTests {

    private lateinit var sut: Kale
    private lateinit var router: IKaleRouter<IKaleIrcMessageHandler>
    private lateinit var tagRouter: IKaleTagRouter
    private lateinit var metadataFactory: IKaleMetadataFactory
    private lateinit var metadata: IMetadataStore

    private lateinit var handlerOne: IKaleIrcMessageHandler
    private lateinit var handlerTwo: IKaleIrcMessageHandler

    @Before fun setUp() {
        router = mock()
        tagRouter = mock()
        metadataFactory = mock()
        metadata = mock()

        whenever(metadataFactory.construct(any())).thenReturn(metadata)

        handlerOne = mock()
        handlerTwo = mock()

        sut = Kale(router, metadataFactory)
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