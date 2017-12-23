package chat.willow.kale.irc.message.rfc1459

import chat.willow.kale.core.message.*

object PongMessage : ICommand {

    override val command = "PONG"

    data class Message(val token: String) {

        object Descriptor : KaleDescriptor<Message>(matcher = commandMatcher(command), parser = Parser)

        object Parser : MessageParser<Message>() {

            override fun parseFromComponents(components: IrcMessageComponents): Message? {
                if (components.parameters.isEmpty()) {
                    return null
                }

                val token = components.parameters[0]

                return Message(token)
            }

        }

        object Serialiser : MessageSerialiser<Message>(command) {

            override fun serialiseToComponents(message: Message): IrcMessageComponents {
                val parameters = listOf(message.token)

                return IrcMessageComponents(parameters)
            }

        }

    }

}