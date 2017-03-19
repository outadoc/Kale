package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl003MessageType = RplSourceTargetContent.Message

object Rpl003Message : ICommand {

    override val command = "003"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)

}