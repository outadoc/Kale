package engineer.carrot.warren.kale.irc.message.utility

import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage
import engineer.carrot.warren.kale.irc.message.IrcMessageParser

data class RawMessage(val line: String) {

    companion object Factory: IMessageSerialiser<RawMessage> {
        override fun serialise(message: RawMessage): IrcMessage? {
            return IrcMessageParser.parse(message.line)
        }
    }
    
}