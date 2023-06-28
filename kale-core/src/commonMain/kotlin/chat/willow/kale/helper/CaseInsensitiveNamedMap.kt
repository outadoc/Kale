package chat.willow.kale.helper

open class CaseInsensitiveNamedMap<NamedType : INamed>(private val mapper: ICaseMapper) {

    private val namedThings = mutableMapOf<String, NamedType>()

    val all: Map<String, NamedType>
        get() = namedThings

    fun put(value: NamedType) {
        namedThings[mapper.toLower(value.name)] = value
    }

    fun remove(key: String): NamedType? {
        return namedThings.remove(mapper.toLower(key))
    }

    fun contains(key: String): Boolean {
        return namedThings.contains(mapper.toLower(key))
    }

    operator fun get(key: String): NamedType? {
        return namedThings[mapper.toLower(key)]
    }

    operator fun plusAssign(namedThing: NamedType) {
        put(namedThing)
    }

    operator fun plusAssign(namedThings: Collection<NamedType>) {
        for (namedThing in namedThings) {
            put(namedThing)
        }
    }

    operator fun minusAssign(key: String) {
        remove(key)
    }

    fun clear() {
        namedThings.clear()
    }

    override fun toString(): String {
        return "CaseInsensitiveNamedMap(namedThings=$namedThings,case=${mapper.current})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CaseInsensitiveNamedMap<*>

        return namedThings == other.namedThings
    }

    override fun hashCode(): Int {
        return namedThings.hashCode()
    }

}