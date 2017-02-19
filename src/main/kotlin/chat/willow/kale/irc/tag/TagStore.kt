package chat.willow.kale.irc.tag

import kotlin.reflect.KClass

interface ITagStore {
    operator fun <T: Any>get(classType: KClass<T>): T?
    operator fun <T: Any>get(classType: Class<T>): T?

    fun <T: Any>store(thing: T)
}

data class TagStore(private val store: MutableMap<Class<*>, Any> = mutableMapOf()) : ITagStore {

    override fun <T : Any> get(classType: Class<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return store[classType] as? T
    }

    override fun <T : Any> get(classType: KClass<T>): T? {
        return this[classType.java]
    }

    override fun <T : Any> store(thing: T) {
        store[thing::class.java] = thing
    }

}

