package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

object Rpl473Message : ICommand {

    override val command = "473"

    class Message(source: String, target: String, channel: String, content: String): RplSourceTargetChannelContent.Message(source, target, channel, content)
    object Parser : RplSourceTargetChannelContent.Parser(command)
    object Serialiser : RplSourceTargetChannelContent.Serialiser(command)
    object Descriptor : RplSourceTargetChannelContent.Descriptor(command, Parser)

}