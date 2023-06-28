package chat.willow.kale

import org.slf4j.LoggerFactory

actual inline fun <reified T : Any> loggerFor(): Logger {
    return Slf4jLogger(LoggerFactory.getLogger(T::class.java))
}
