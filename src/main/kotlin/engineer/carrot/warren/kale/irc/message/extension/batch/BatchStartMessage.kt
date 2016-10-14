package engineer.carrot.warren.kale.irc.message.extension.batch

import engineer.carrot.warren.kale.irc.CharacterCodes
import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageParser
import engineer.carrot.warren.kale.irc.message.IMessageSerialiser
import engineer.carrot.warren.kale.irc.message.IrcMessage

data class BatchStartMessage(val reference: String, val type: String, val parameters: List<String> = listOf()): IMessage {

    override val command = "BATCH"

    companion object Factory: IMessageParser<BatchStartMessage>, IMessageSerialiser<BatchStartMessage> {

        override fun serialise(message: BatchStartMessage): IrcMessage? {
            return IrcMessage(command = message.command, parameters = listOf(CharacterCodes.PLUS + message.reference, message.type) + message.parameters)
        }

        override fun parse(message: IrcMessage): BatchStartMessage? {
            if (message.parameters.size < 2) {
                return null
            }

            if (message.parameters[0].length <= 1) {
                return null
            }

            if (message.parameters[0][0] != CharacterCodes.PLUS) {
                return null
            }

            val reference = message.parameters[0].drop(1)
            val type = message.parameters[1]
            val parameters = message.parameters.drop(2)

            return BatchStartMessage(reference = reference, type = type, parameters = parameters)
        }

    }

}