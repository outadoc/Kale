package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl372MessageType = RplSourceTargetContent.Message

object Rpl372Message : ICommand {

    override val command = "372"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)

}