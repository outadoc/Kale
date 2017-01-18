package chat.willow.kale.irc.message.extension.batch

import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage

data class BatchEndMessage(val reference: String): IMessage {

    override val command = "BATCH"

    companion object Factory: IMessageParser<BatchEndMessage>, IMessageSerialiser<BatchEndMessage> {

        override fun serialise(message: BatchEndMessage): IrcMessage? {
            return IrcMessage(command = message.command, parameters = listOf(CharacterCodes.MINUS + message.reference))
        }

        override fun parse(message: IrcMessage): BatchEndMessage? {
            if (message.parameters.isEmpty()) {
                return null
            }

            if (message.parameters[0].length <= 1) {
                return null
            }

            if (message.parameters[0][0] != CharacterCodes.MINUS) {
                return null
            }

            val reference = message.parameters[0].drop(1)

            return BatchEndMessage(reference = reference)
        }

    }

}