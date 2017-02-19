package chat.willow.kale

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.tag.ITagStore

interface IKaleHandler<T: IMessage> {
    val messageType: Class<T>

    fun handle(message: T, tags: ITagStore)
}