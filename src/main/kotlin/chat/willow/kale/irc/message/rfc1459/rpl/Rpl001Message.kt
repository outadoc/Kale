package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl001MessageType = RplSourceTargetContent.Message

object Rpl001Message : ICommand {

    override val command = "001"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)
    object Descriptor : RplSourceTargetContent.Descriptor(command, Parser)

}