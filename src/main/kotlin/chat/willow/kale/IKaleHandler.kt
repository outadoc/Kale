package chat.willow.kale

import chat.willow.kale.irc.message.IMessage

interface IKaleHandler<T: IMessage> {
    val messageType: Class<T>

    fun handle(message: T, tags: Map<String, String?>)
}