package engineer.carrot.warren.pellet.irc.message

data class IrcMessage(val tags: Map<String, String?> = emptyMap(), val prefix: String? = null, val command: String, val parameters: List<String> = emptyList())