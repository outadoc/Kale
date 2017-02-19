package chat.willow.kale.irc.tag

import chat.willow.kale.irc.tag.extension.AccountTag
import chat.willow.kale.irc.tag.extension.ServerTimeTag
import kotlin.reflect.KClass

interface IKaleTagRouter {

    fun <T> routeTagToParser(name: String, parser: ITagParser<T>)
    fun parserFor(name: String): ITagParser<*>?

    fun <T: Any> routeTagToSerialiser(tagClass: KClass<T>, serialiser: ITagSerialiser<T>)
    fun <T: Any> serialiserFor(tagClass: Class<T>): ITagSerialiser<T>?

}

class KaleTagRouter: IKaleTagRouter {

    private val namesToParsers = hashMapOf<String, ITagParser<*>>()
    private val tagsToSerialisers = hashMapOf<Class<*>, ITagSerialiser<*>>()

    override fun <T> routeTagToParser(name: String, parser: ITagParser<T>) {
        namesToParsers[name] = parser
    }

    override fun parserFor(name: String): ITagParser<*>? {
        return namesToParsers[name]
    }

    override fun <T : Any> routeTagToSerialiser(tagClass: KClass<T>, serialiser: ITagSerialiser<T>) {
        tagsToSerialisers[tagClass.java] = serialiser
    }

    override fun <T : Any> serialiserFor(tagClass: Class<T>): ITagSerialiser<T>? {
        @Suppress("UNCHECKED_CAST")
        return tagsToSerialisers[tagClass] as? ITagSerialiser<T>
    }

    fun useDefaults(): KaleTagRouter {
        routeTagToParser(AccountTag.name, AccountTag.Factory)
        routeTagToSerialiser(AccountTag::class, AccountTag.Factory)

        routeTagToParser(ServerTimeTag.name, ServerTimeTag.Factory)
        routeTagToSerialiser(ServerTimeTag::class, ServerTimeTag.Factory)

        return this
    }

}