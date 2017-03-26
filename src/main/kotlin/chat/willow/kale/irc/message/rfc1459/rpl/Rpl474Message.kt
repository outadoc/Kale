package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl474MessageType = RplSourceTargetContent.Message

object Rpl474Message : ICommand {

    override val command = "474"

    object Parser : RplSourceTargetChannelContent.Parser(command)
    object Serialiser : RplSourceTargetChannelContent.Serialiser(command)

}