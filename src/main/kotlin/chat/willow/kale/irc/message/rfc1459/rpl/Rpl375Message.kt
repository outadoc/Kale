package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl375MessageType = RplSourceTargetContent.Message

object Rpl375Message : ICommand {

    override val command = "375"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)
    object Descriptor : RplSourceTargetContent.Descriptor(command, Parser)

}