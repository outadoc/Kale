package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl475MessageType = RplSourceTargetChannelContent.Message

object Rpl475Message : ICommand {

    override val command = "475"

    object Parser : RplSourceTargetChannelContent.Parser(command)
    object Serialiser : RplSourceTargetChannelContent.Serialiser(command)
    object Descriptor : RplSourceTargetChannelContent.Descriptor(command, Parser)

}