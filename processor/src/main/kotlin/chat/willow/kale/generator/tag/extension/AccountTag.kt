package chat.willow.kale.generator.tag.extension

import chat.willow.kale.generator.tag.ITagParser
import chat.willow.kale.generator.tag.ITagSerialiser
import chat.willow.kale.generator.tag.Tag

data class AccountTag(val account: String) {

    companion object Factory: ITagParser<AccountTag>, ITagSerialiser<AccountTag> {

        val name = "account"

        override fun parse(tag: Tag): AccountTag? {
            val value = tag.value ?: return null

            return AccountTag(account = value)
        }

        override fun serialise(tag: AccountTag): Tag? {
            return Tag(name = name, value = tag.account)
        }

    }

}