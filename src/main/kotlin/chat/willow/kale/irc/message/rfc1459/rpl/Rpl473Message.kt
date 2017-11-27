package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

typealias Rpl473MessageType = RplSourceTargetChannelContent.Message

object Rpl473Message : ICommand {

    override val command = "473"

    object Parser : RplSourceTargetChannelContent.Parser(command)
    object Serialiser : RplSourceTargetChannelContent.Serialiser(command)
    object Descriptor : RplSourceTargetChannelContent.Descriptor(command, Parser)

}