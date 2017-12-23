package chat.willow.kale.irc.message.rfc1459.rpl

import chat.willow.kale.generator.SourceTargetChannelContent
import chat.willow.kale.generator.SourceTargetContent

internal object NumericRegistry {
    @SourceTargetContent(numeric = "001")        object WELCOME
    @SourceTargetContent(numeric = "002")        object YOURHOST
    @SourceTargetContent(numeric = "003")        object CREATED
    @SourceTargetContent(numeric = "372")        object MOTD
    @SourceTargetContent(numeric = "375")        object MOTDSTART
    @SourceTargetContent(numeric = "376")        object ENDOFMOTD
    @SourceTargetContent(numeric = "422")        object NOMOTD
    @SourceTargetContent(numeric = "433")        object NICKNAMEINUSE
    @SourceTargetChannelContent(numeric = "331") object NOTOPIC
    @SourceTargetChannelContent(numeric = "332") object TOPIC
    @SourceTargetChannelContent(numeric = "366") object ENDOFNAMES
    @SourceTargetChannelContent(numeric = "471") object CHANNELISFULL
}