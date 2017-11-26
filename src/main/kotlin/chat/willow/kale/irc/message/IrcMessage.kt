package chat.willow.kale.irc.message

data class IrcMessage(val tags: Map<String, String?> = emptyMap(), val prefix: String? = null, val command: String, val parameters: List<String> = emptyList()) {
    override fun toString(): String {
        val pieces = mutableListOf<String>()

        if (!tags.isEmpty()) {
            pieces += "tags=$tags"
        }

        if (!prefix.isNullOrEmpty()) {
            pieces += "prefix=$prefix"
        }

        pieces += "command=$command"

        if (!parameters.isEmpty()) {
            pieces += "parameters=$parameters"
        }

        return "IrcMessage(${pieces.joinToString()})"
    }
}