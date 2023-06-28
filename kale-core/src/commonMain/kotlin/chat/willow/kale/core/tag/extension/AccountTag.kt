package chat.willow.kale.core.tag.extension

import chat.willow.kale.core.tag.ITagParser
import chat.willow.kale.core.tag.ITagSerialiser
import chat.willow.kale.core.tag.Tag

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