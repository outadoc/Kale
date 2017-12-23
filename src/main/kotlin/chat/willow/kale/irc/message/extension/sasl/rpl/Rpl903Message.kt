package chat.willow.kale.irc.message.extension.sasl.rpl

import chat.willow.kale.core.ICommand
import chat.willow.kale.core.RplSourceTargetContent

typealias Rpl903MessageType = RplSourceTargetContent.Message

object Rpl903Message : ICommand {

    override val command = "903"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)
    object Descriptor : RplSourceTargetContent.Descriptor(command, Parser)

}