package chat.willow.kale.generator.tag

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TagStoreTests {

    private lateinit var sut: TagStore

    @Before fun setUp() {
        sut = TagStore()
    }

    @Test fun test_get_javaClass() {
        sut.store(java.lang.Integer(1234))

        val value = sut[java.lang.Integer::class.java]

        assertEquals(1234, value)
    }

    @Test fun test_get_kotlinClass() {
        sut.store(java.lang.Integer(5678))

        val value = sut[java.lang.Integer::class]

        assertEquals(5678, value)
    }

    @Test fun test_store_MultipleThings() {
        sut.store(java.lang.Integer(9))
        sut.store(java.lang.Boolean(true))

        val firstValue = sut[java.lang.Integer::class]
        val secondValue = sut[java.lang.Boolean::class]

        assertEquals(9, firstValue)
        assertEquals(true, secondValue)
    }

    @Test fun test_store_SameType_OverwritesPreviousValue() {
        sut.store(java.lang.Integer(1))
        sut.store(java.lang.Integer(2))

        val value = sut[java.lang.Integer::class]

        assertEquals(2, value)
    }
}