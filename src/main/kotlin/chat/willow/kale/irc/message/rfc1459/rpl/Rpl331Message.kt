package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl331MessageType = RplSourceTargetContent.Message

object Rpl331Message : ICommand {

    override val command = "331"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)
    object Descriptor : RplSourceTargetContent.Descriptor(command, Parser)

}