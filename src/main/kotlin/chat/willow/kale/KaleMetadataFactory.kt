package chat.willow.kale

import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.tag.IKaleTagRouter
import chat.willow.kale.irc.tag.ITagStore
import chat.willow.kale.irc.tag.Tag
import chat.willow.kale.irc.tag.TagStore

typealias KaleMetadataStore = ITagStore

interface IKaleMetadataFactory {

    fun construct(message: IrcMessage): KaleMetadataStore

}

class KaleMetadataFactory(private val tagRouter: IKaleTagRouter): IKaleMetadataFactory {

    private val LOGGER = loggerFor<KaleMetadataFactory>()

    override fun construct(message: IrcMessage): KaleMetadataStore {
        val metadata = TagStore()

        for ((key, value) in message.tags) {
            val tag = Tag(key, value)

            val factory = tagRouter.parserFor(tag.name)
            if (factory == null) {
                LOGGER.debug("no parser for tag $tag")
                continue
            }

            val parsedTag = factory.parse(tag)
            if (parsedTag == null) {
                LOGGER.warn("factory failed to parse tag: $factory $tag")
                continue
            }

            metadata.store(parsedTag)
        }

        return metadata
    }

}