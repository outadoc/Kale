package chat.willow.kale

import chat.willow.kale.irc.tag.ITagStore

typealias IMetadataStore = ITagStore

interface ICommand {

    val command: String

}

interface ISubcommand {

    val subcommand: String
}

data class IrcMessageComponents(val parameters: List<String> = listOf(), val tags: Map<String, String?> = mapOf(), val prefix: String? = null)