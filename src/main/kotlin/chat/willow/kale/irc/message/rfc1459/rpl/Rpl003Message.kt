package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.ICommand

object Rpl003Message : ICommand {

    override val command = "003"

    class Message(source: String, target: String, content: String): RplSourceTargetContent.Message(source, target, content)
    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)
    object Descriptor : RplSourceTargetContent.Descriptor(command, Parser)

}