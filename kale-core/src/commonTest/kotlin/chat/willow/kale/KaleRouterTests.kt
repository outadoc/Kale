package chat.willow.kale

import chat.willow.kale.core.message.IMessageSerialiser
import org.kodein.mock.Mock
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class KaleRouterTests : TestsWithMocks() {
    override fun setUpMocks() = injectMocks(mocker)


    private lateinit var sut: KaleRouter

    @Mock
    lateinit var serialiserOne: IMessageSerialiser<Int>

    @Mock
    lateinit var serialiserTwo: IMessageSerialiser<String>

    @Mock
    lateinit var newSerialiserOne: IMessageSerialiser<Int>

    @BeforeTest
    fun setUp() {
        sut = KaleRouter()
    }

    @Test
    fun test_serialiserFor_MessageNotRegistered_ReturnsNull() {
        val serialiser = sut.serialiserFor(Int::class)

        assertNull(serialiser)
    }

    @Test
    fun test_serialiserFor_MessageRegistered_ReturnsCorrectSerialiser() {
        sut.register(Int::class, serialiserOne)

        val serialiser = sut.serialiserFor(Int::class)

        assertTrue(serialiserOne === serialiser)
    }

    @Test
    fun test_serialiserFor_DifferentMessageRegistered_ReturnsCorrectSerialiser() {
        sut.register(Int::class, serialiserOne)
        sut.register(String::class, serialiserTwo)
        sut.register(Int::class, newSerialiserOne)

        val serialiser = sut.serialiserFor(Int::class)

        assertTrue(newSerialiserOne === serialiser)
    }

}