package chat.willow.kale.core.message

interface ISubcommand {

    val subcommand: String
}

data class IrcMessageComponents(val parameters: List<String> = listOf(), val tags: Map<String, String?> = mapOf(), val prefix: String? = null)