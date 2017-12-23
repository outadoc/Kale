package chat.willow.kale.core.tag.extension

import chat.willow.kale.core.tag.ITagParser
import chat.willow.kale.core.tag.ITagSerialiser
import chat.willow.kale.core.tag.Tag
import chat.willow.kale.loggerFor
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

data class ServerTimeTag(val time: Instant) {

    companion object Factory: ITagParser<ServerTimeTag>, ITagSerialiser<ServerTimeTag> {

        private val LOGGER = loggerFor<Factory>()

        private val timeZone = TimeZone.getTimeZone("UTC")
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss'Z'")

        val name = "time"

        init {
            dateFormat.timeZone = timeZone
        }

        override fun parse(tag: Tag): ServerTimeTag? {
            val value = tag.value ?: return null

            val instant = try {
                Instant.parse(value)
            } catch (exception: Exception) {
                LOGGER.warn("failed to parse date for tag: $tag $exception")

                null
            } ?: return null

            return ServerTimeTag(time = instant)
        }

        override fun serialise(tag: ServerTimeTag): Tag? {
            val instant = tag.time.toString()

            return Tag(name = name, value = instant)
        }

    }

}