package chat.willow.kale.irc.message.utility

import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.message.IrcMessageParser

object RawMessage {

    data class Line(val line: String) {

        object Serialiser : IMessageSerialiser<Line> {

            override fun serialise(message: Line): IrcMessage? {
                return IrcMessageParser.parse(message.line)
            }
        }

    }

}