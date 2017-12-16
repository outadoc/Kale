package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

object Rpl332Message : ICommand {

    override val command = "332"

    class Message(source: String, target: String, content: String): RplSourceTargetContent.Message(source, target, content)
    object Parser : RplSourceTargetChannelContent.Parser(command)
    object Serialiser : RplSourceTargetChannelContent.Serialiser(command)
    object Descriptor : RplSourceTargetChannelContent.Descriptor(command, Parser)

}