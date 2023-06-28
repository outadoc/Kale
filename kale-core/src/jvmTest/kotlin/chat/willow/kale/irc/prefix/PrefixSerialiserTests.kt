package chat.willow.kale.irc.prefix

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PrefixSerialiserTests {
    lateinit var prefixSerialiser: IPrefixSerialiser

    @Before fun setUp() {
        prefixSerialiser = PrefixSerialiser
    }

    @Test fun test_serialise_NickOnly() {
        val prefix = prefixSerialiser.serialise(Prefix(nick = "nick"))

        assertEquals("nick", prefix)
    }

    @Test fun test_serialise_NickAndUser() {
        val prefix = prefixSerialiser.serialise(Prefix(nick = "nick", user = "user"))

        assertEquals("nick!user", prefix)
    }

    @Test fun test_serialise_NickAndHost() {
        val prefix = prefixSerialiser.serialise(Prefix(nick = "nick", host = "host"))

        assertEquals("nick@host", prefix)
    }

    @Test fun test_serialise_NickUserAndHost() {
        val prefix = prefixSerialiser.serialise(Prefix(nick = "nick", user = "user", host = "host"))

        assertEquals("nick!user@host", prefix)
    }
}