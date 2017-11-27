package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl376MessageType = RplSourceTargetContent.Message

object Rpl376Message : ICommand {

    override val command = "376"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)
    object Descriptor : RplSourceTargetContent.Descriptor(command, Parser)

}