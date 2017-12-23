package chat.willow.kale.generator.message

interface ICommand {

    val command: String

}

interface ISubcommand {

    val subcommand: String
}

data class IrcMessageComponents(val parameters: List<String> = listOf(), val tags: Map<String, String?> = mapOf(), val prefix: String? = null)