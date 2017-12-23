package chat.willow.kale.core.tag

data class Tag(val name: String, val value: String? = null)

interface ITagParser<out T> {
    fun parse(tag: Tag): T?
}

interface ITagSerialiser<in T> {
    fun serialise(tag: T): Tag?
}