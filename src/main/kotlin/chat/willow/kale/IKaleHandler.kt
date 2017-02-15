package chat.willow.kale

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.metadata.IMetadataStore

interface IKaleHandler<T: IMessage> {
    val messageType: Class<T>

    fun handle(message: T, metadata: IMetadataStore)
}