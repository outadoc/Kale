package chat.willow.kale

import chat.willow.kale.generator.message.IMessageSerialiser
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KaleRouterTests {

    private lateinit var sut: KaleRouter

    @Before fun setUp() {
        sut = KaleRouter()
    }

    @Test fun test_serialiserFor_MessageNotRegistered_ReturnsNull() {
        val serialiser = sut.serialiserFor(Int::class.java)

        assertNull(serialiser)
    }

    @Test fun test_serialiserFor_MessageRegistered_ReturnsCorrectSerialiser() {
        val serialiserOne: IMessageSerialiser<Int> = mock()
        sut.register(Int::class, serialiserOne)

        val serialiser = sut.serialiserFor(Int::class.java)

        assertTrue(serialiserOne === serialiser)
    }

    @Test fun test_serialiserFor_DifferentMessageRegistered_ReturnsCorrectSerialiser() {
        val serialiserOne: IMessageSerialiser<Int> = mock()
        val serialiserTwo: IMessageSerialiser<String> = mock()
        val newSerialiserOne: IMessageSerialiser<Int> = mock()
        sut.register(Int::class, serialiserOne)
        sut.register(String::class, serialiserTwo)
        sut.register(Int::class, newSerialiserOne)

        val serialiser = sut.serialiserFor(Int::class.java)

        assertTrue(newSerialiserOne === serialiser)
    }

}