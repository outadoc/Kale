package engineer.carrot.warren.kale.irc.message.extension.batch

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class BatchEndMessage(val reference: String): IMessage {

    override val command = "BATCH"

    companion object Factory: IMessageParser<BatchEndMessage>, IMessageSerialiser<BatchEndMessage> {

        override fun serialise(message: BatchEndMessage): IrcMessage? {
            return IrcMessage(command = message.command, parameters = listOf(CharacterCodes.MINUS + message.reference))
        }

        override fun parse(message: IrcMessage): BatchEndMessage? {
            if (message.parameters.size < 1) {
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