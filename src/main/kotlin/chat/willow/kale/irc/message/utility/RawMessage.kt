package chat.willow.kale.irc.message.utility

import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.message.IrcMessageParser

data class RawMessage(val line: String) {

    companion object Factory: IMessageSerialiser<RawMessage> {
        override fun serialise(message: RawMessage): IrcMessage? {
            return IrcMessageParser.parse(message.line)
        }
    }
    
}