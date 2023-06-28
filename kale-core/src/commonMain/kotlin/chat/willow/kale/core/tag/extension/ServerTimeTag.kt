package chat.willow.kale.core.tag.extension

import chat.willow.kale.core.tag.ITagParser
import chat.willow.kale.core.tag.ITagSerialiser
import chat.willow.kale.core.tag.Tag
import chat.willow.kale.loggerFor
import kotlinx.datetime.Instant

data class ServerTimeTag(val time: Instant) {

    companion object Factory : ITagParser<ServerTimeTag>, ITagSerialiser<ServerTimeTag> {

        private val LOGGER = loggerFor<Factory>()

        const val name = "time"

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