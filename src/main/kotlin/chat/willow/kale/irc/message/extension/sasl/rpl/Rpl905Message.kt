package chat.willow.kale.irc.message.extension.sasl.rpl

import chat.willow.kale.generator.message.ICommand
import chat.willow.kale.generator.RplSourceTargetContent

typealias Rpl905MessageType = RplSourceTargetContent.Message

object Rpl905Message : ICommand {

    override val command = "905"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)
    object Descriptor : RplSourceTargetContent.Descriptor(command, Parser)

}