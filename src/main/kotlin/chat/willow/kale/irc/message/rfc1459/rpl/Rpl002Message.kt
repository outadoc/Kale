package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl002MessageType = RplSourceTargetContent.Message

object Rpl002Message : ICommand {

    override val command = "002"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)

}