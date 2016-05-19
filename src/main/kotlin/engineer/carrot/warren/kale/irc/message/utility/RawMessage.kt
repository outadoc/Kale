package engineer.carrot.warren.kale.irc.message.utility

import engineer.carrot.warren.kale.irc.message.*

data class RawMessage(val line: String): IMessage {

    companion object Factory: IMessageFactory<RawMessage> {
        override val messageType = RawMessage::class.java
        override val key = "RAW-NOOP"

        override fun serialise(message: RawMessage): IrcMessage? {
            return IrcMessageParser.parse(message.line)
        }

        override fun parse(message: IrcMessage): RawMessage? {
            return RawMessage(IrcMessageSerialiser.serialise(message) ?: "")
        }
    }
    
}