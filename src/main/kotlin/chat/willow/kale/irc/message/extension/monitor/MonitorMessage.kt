package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.generator.message.*
import chat.willow.kale.irc.CharacterCodes

object MonitorMessage : ICommand {

    override val command = "MONITOR"

    object Add : ISubcommand {

        override val subcommand = CharacterCodes.PLUS.toString()

        data class Command(val targets: List<String>) {

            object Descriptor : KaleDescriptor<Command>(matcher = subcommandMatcher(command, subcommand, subcommandPosition = 0), parser = Parser)

            object Parser : SubcommandParser<Command>(subcommand) {

                override fun parseFromComponents(components: IrcMessageComponents): Command? {
                    if (components.parameters.isEmpty()) {
                        return null
                    }

                    val rawTargets = components.parameters[0]

                    val targets = rawTargets.split(delimiters = CharacterCodes.COMMA)

                    return Command(targets)
                }
            }

            object Serialiser : SubcommandSerialiser<Command>(command, subcommand) {

                override fun serialiseToComponents(message: Command): IrcMessageComponents {
                    val targets = message.targets.joinToString(separator = CharacterCodes.COMMA.toString())

                    return IrcMessageComponents(parameters = listOf(targets))
                }

            }

        }

    }

    object Clear : ISubcommand {

        override val subcommand = "C"

        object Command {

            object Descriptor : KaleDescriptor<Command>(matcher = subcommandMatcher(command, subcommand, subcommandPosition = 0), parser = Parser)

            object Parser : SubcommandParser<Command>(subcommand) {

                override fun parseFromComponents(components: IrcMessageComponents): Command? {
                    return Command
                }

            }

            object Serialiser : SubcommandSerialiser<Command>(command, subcommand) {

                override fun serialiseToComponents(message: Command): IrcMessageComponents {
                    return IrcMessageComponents()
                }
            }

        }

    }

    object ListAll : ISubcommand {

        override val subcommand = "L"

        // TODO: same as Clear

        object Command {

            object Descriptor : KaleDescriptor<Command>(matcher = subcommandMatcher(command, subcommand, subcommandPosition = 0), parser = Parser)

            object Parser : SubcommandParser<Command>(subcommand) {

                override fun parseFromComponents(components: IrcMessageComponents): Command? {
                    return Command
                }

            }

            object Serialiser : SubcommandSerialiser<Command>(command, subcommand) {

                override fun serialiseToComponents(message: Command): IrcMessageComponents {
                    return IrcMessageComponents()
                }
            }

        }

    }

    object Remove : ISubcommand {

        override val subcommand = CharacterCodes.MINUS.toString()
        
        // TODO: same as Add

        data class Command(val targets: List<String>) {

            object Descriptor : KaleDescriptor<Command>(matcher = subcommandMatcher(command, subcommand, subcommandPosition = 0), parser = Parser)

            object Parser : SubcommandParser<Command>(subcommand) {

                override fun parseFromComponents(components: IrcMessageComponents): Command? {
                    if (components.parameters.isEmpty()) {
                        return null
                    }

                    val rawTargets = components.parameters[0]

                    val targets = rawTargets.split(delimiters = CharacterCodes.COMMA)

                    return Command(targets)
                }
            }

            object Serialiser : SubcommandSerialiser<Command>(command, subcommand) {

                override fun serialiseToComponents(message: Command): IrcMessageComponents {
                    val targets = message.targets.joinToString(separator = CharacterCodes.COMMA.toString())

                    return IrcMessageComponents(parameters = listOf(targets))
                }
                
            }

        }

    }

    object Status : ISubcommand {

        override val subcommand = "S"

        // TODO: same as Clear

        object Command {

            object Descriptor : KaleDescriptor<Command>(matcher = subcommandMatcher(command, subcommand, subcommandPosition = 0), parser = Parser)

            object Parser : SubcommandParser<Command>(subcommand) {

                override fun parseFromComponents(components: IrcMessageComponents): Command? {
                    return Command
                }

            }

            object Serialiser : SubcommandSerialiser<Command>(command, subcommand) {

                override fun serialiseToComponents(message: Command): IrcMessageComponents {
                    return IrcMessageComponents()
                }
            }

        }
        
    }

}