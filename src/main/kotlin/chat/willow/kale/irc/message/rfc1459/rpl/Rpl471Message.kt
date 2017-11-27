package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl471MessageType = RplSourceTargetChannelContent.Message

object Rpl471Message : ICommand {

    override val command = "471"

    object Parser : RplSourceTargetChannelContent.Parser(command)
    object Serialiser : RplSourceTargetChannelContent.Serialiser(command)
    object Descriptor : RplSourceTargetChannelContent.Descriptor(command, Parser)

}