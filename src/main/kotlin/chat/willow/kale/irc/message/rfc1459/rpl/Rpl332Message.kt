package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl332MessageType = RplSourceTargetChannelContent.Message

object Rpl332Message : ICommand {

    override val command = "332"

    object Parser : RplSourceTargetChannelContent.Parser(Rpl471Message.command)
    object Serialiser : RplSourceTargetChannelContent.Serialiser(Rpl471Message.command)

}