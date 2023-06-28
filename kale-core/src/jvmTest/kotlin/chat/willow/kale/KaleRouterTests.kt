package chat.willow.kale

import chat.willow.kale.core.message.IMessageSerialiser
import com.nhaarman.mockito_kotlin.mock
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test

class KaleRouterTests {

    private lateinit var sut: KaleRouter

    @BeforeTest fun setUp() {
        sut = KaleRouter()
    }

    @Test fun test_serialiserFor_MessageNotRegistered_ReturnsNull() {
        val serialiser = sut.serialiserFor(Int::class)

        assertNull(serialiser)
    }

    @Test fun test_serialiserFor_MessageRegistered_ReturnsCorrectSerialiser() {
        val serialiserOne: IMessageSerialiser<Int> = mock()
        sut.register(Int::class, serialiserOne)

        val serialiser = sut.serialiserFor(Int::class)

        assertTrue(serialiserOne === serialiser)
    }

    @Test fun test_serialiserFor_DifferentMessageRegistered_ReturnsCorrectSerialiser() {
        val serialiserOne: IMessageSerialiser<Int> = mock()
        val serialiserTwo: IMessageSerialiser<String> = mock()
        val newSerialiserOne: IMessageSerialiser<Int> = mock()
        sut.register(Int::class, serialiserOne)
        sut.register(String::class, serialiserTwo)
        sut.register(Int::class, newSerialiserOne)

        val serialiser = sut.serialiserFor(Int::class)

        assertTrue(newSerialiserOne === serialiser)
    }

}