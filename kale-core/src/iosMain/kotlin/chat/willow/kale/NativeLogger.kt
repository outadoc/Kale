package chat.willow.kale

class NativeLogger(private val tag: String) : Logger {
    override fun warn(message: String) {}
    override fun debug(message: String) {}
}
