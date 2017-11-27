package chat.willow.kale.irc.message.extension.sasl

import chat.willow.kale.ICommand
import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.KaleDescriptor
import chat.willow.kale.commandMatcher
import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser

object AuthenticateMessage : ICommand {

    override val command = "AUTHENTICATE"

    data class Command(val payload: String) {

        object Descriptor : KaleDescriptor<Command>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Command>() {

            override fun parseFromComponents(components: IrcMessageComponents): Command? {
                if (components.parameters.isEmpty()) {
                    return null
                }

                val payload = components.parameters[0]

                return Command(payload)
            }
        }

        object Serialiser : MessageSerialiser<Command>(command) {

            override fun serialiseToComponents(message: Command): IrcMessageComponents {
                return IrcMessageComponents(parameters = listOf(message.payload))
            }

        }

    }

    data class Message(val payload: String, val isEmpty: Boolean) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.isEmpty()) {
                    return null
                }

                val payload = components.parameters[0]
                val isEmpty = payload == "${CharacterCodes.PLUS}"

                return Message(payload, isEmpty)
            }
        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                return if (message.isEmpty) {
                    IrcMessageComponents(parameters = listOf("${CharacterCodes.PLUS}"))
                } else {
                    IrcMessageComponents(parameters = listOf(message.payload))
                }
            }
        }

    }

}