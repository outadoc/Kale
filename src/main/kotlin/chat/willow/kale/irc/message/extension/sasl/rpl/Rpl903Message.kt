package chat.willow.kale.irc.message.extension.sasl.rpl

import chat.willow.kale.ICommand
import chat.willow.kale.irc.message.rfc1459.rpl.RplSourceTargetContent

typealias Rpl903MessageType = RplSourceTargetContent.Message

object Rpl903Message : ICommand {

    override val command = "903"

    object Parser : RplSourceTargetContent.Parser(command)
    object Serialiser : RplSourceTargetContent.Serialiser(command)

}