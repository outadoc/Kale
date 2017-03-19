package chat.willow.kale.irc.message.extension.sasl.rpl

import chat.willow.kale.ICommand
import chat.willow.kale.irc.message.rfc1459.rpl.RplSourceTargetContent

typealias Rpl905MessageType = RplSourceTargetContent.Message

object Rpl905Message : ICommand {

    override val command = "905"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)

}