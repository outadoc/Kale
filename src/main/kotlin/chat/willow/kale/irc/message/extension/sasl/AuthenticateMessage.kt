package chat.willow.kale.irc.message.extension.sasl

import chat.willow.kale.ICommand
import chat.willow.kale.IrcMessageComponents
import chat.willow.kale.irc.CharacterCodes
import chat.willow.kale.irc.message.MessageParser
import chat.willow.kale.irc.message.MessageSerialiser

object AuthenticateMessage : ICommand {

    override val command = "AUTHENTICATE"

    data class Command(val payload: String) {

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
                if (message.isEmpty) {
                    return IrcMessageComponents(parameters = listOf("${CharacterCodes.PLUS}"))
                } else {
                    return IrcMessageComponents(parameters = listOf(message.payload))
                }
            }
        }

    }

}