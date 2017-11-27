package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl422MessageType = RplSourceTargetContent.Message

object Rpl422Message : ICommand {

    override val command = "422"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)
    object Descriptor : RplSourceTargetContent.Descriptor(command, Parser)

}