package chat.willow.kale.irc.message.extension.sasl.rpl

import chat.willow.kale.core.ICommand
import chat.willow.kale.core.RplSourceTargetContent

typealias Rpl905MessageType = RplSourceTargetContent.Message

object Rpl905Message : ICommand {

    override val command = "905"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)
    object Descriptor : RplSourceTargetContent.Descriptor(command, Parser)

}