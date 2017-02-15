package chat.willow.kale.irc.metadata

import kotlin.reflect.KClass

interface IMetadataStore {
    operator fun <T: Any>get(classType: KClass<T>): T?
    operator fun <T: Any>get(classType: Class<T>): T?

    fun <T: Any>store(thing: T)
}

class MetadataStore: IMetadataStore {

    private val metadata = mutableMapOf<Class<*>, Any>()

    override fun <T : Any> get(classType: Class<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return metadata[classType] as? T
    }

    override fun <T : Any> get(classType: KClass<T>): T? {
        return this[classType.java]
    }

    override fun <T : Any> store(thing: T) {
        metadata[thing::class.java] = thing
    }

}

