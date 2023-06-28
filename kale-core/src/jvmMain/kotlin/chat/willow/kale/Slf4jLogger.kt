package chat.willow.kale

class Slf4jLogger(private val logger: org.slf4j.Logger) : Logger {
    override fun warn(message: String) = logger.warn(message)
    override fun debug(message: String) = logger.debug(message)
}
