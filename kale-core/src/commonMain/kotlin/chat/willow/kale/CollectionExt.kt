package chat.willow.kale

internal fun <T> MutableList<T>.poll(): T {
    val item = this[0]
    this.removeAt(0)

    return item
}
