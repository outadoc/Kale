package chat.willow.kale

class NoOpLogger : Logger {
    override fun warn(message: String) {}
    override fun debug(message: String) {}
}
