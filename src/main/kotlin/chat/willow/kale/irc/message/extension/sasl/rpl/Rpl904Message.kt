package chat.willow.kale.irc.message.extension.sasl.rpl

import chat.willow.kale.generator.message.ICommand
import chat.willow.kale.generator.RplSourceTargetContent

typealias Rpl904MessageType = RplSourceTargetContent.Message

object Rpl904Message : ICommand {

    override val command = "904"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)
    object Descriptor : RplSourceTargetContent.Descriptor(command, Parser)

}