package chat.willow.kale

actual inline fun <reified T : Any> loggerFor(): Logger {
    return NoOpLogger()
}
