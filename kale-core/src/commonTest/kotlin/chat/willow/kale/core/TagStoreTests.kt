package chat.willow.kale.core

import chat.willow.kale.core.tag.TagStore
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test

class TagStoreTests {

    private lateinit var sut: TagStore

    @BeforeTest fun setUp() {
        sut = TagStore()
    }

    @Test fun test_get_javaClass() {
        sut.store(1234)

        val value = sut[Int::class]

        assertEquals(1234, value)
    }

    @Test fun test_get_kotlinClass() {
        sut.store(5678)

        val value = sut[Int::class]

        assertEquals(5678, value)
    }

    @Test fun test_store_MultipleThings() {
        sut.store(9)
        sut.store(true)

        val firstValue = sut[Int::class]
        val secondValue = sut[Boolean::class]

        assertEquals(9, firstValue)
        assertEquals(true, secondValue)
    }

    @Test fun test_store_SameType_OverwritesPreviousValue() {
        sut.store(1)
        sut.store(2)

        val value = sut[Int::class]

        assertEquals(2, value)
    }
}