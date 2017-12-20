package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.generator.SourceTargetChannelContent
import chat.willow.kale.generator.SourceTargetContent

object NumericRegistry {
    @SourceTargetContent(numeric = "001")        object WELCOME
    @SourceTargetChannelContent(numeric = "366") object ENDOFNAMES

    // todo: must also register these, or store them somewhere such that they are registerable
}