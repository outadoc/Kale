package chat.willow.kale.core.tag

import kotlin.reflect.KClass

interface ITagStore {
    operator fun <T: Any>get(classType: KClass<T>): T?

    fun <T: Any>store(thing: T)
}

data class TagStore(private val store: MutableMap<KClass<*>, Any> = mutableMapOf()) : ITagStore {

    override fun <T : Any> get(classType: KClass<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return store[classType] as? T
    }

    override fun <T : Any> store(thing: T) {
        store[thing::class] = thing
    }

    override fun toString(): String {
        val content = if (store.isEmpty()) {
            "Empty"
        } else {
            "content=$store"
        }

        return "TagStore($content)"
    }
}

