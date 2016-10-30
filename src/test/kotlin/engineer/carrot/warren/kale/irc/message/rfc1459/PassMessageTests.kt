package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class PassMessageTests {

    lateinit var factory: PassMessage.Factory

    @Before fun setUp() {
        factory = PassMessage.Factory
    }

    @Test fun test_parse() {
        val message = factory.parse(IrcMessage(command = "PASS", parameters = listOf("password")))

        assertEquals(message, PassMessage(password = "password"))
    }

    @Test fun test_parse_tooFewParameters() {
        val message = factory.parse(IrcMessage(command = "PASS", parameters = listOf()))

        assertNull(message)
    }

    @Test fun test_serialise() {
        val message = factory.serialise(PassMessage(password = "password"))

        assertEquals(message, IrcMessage(command = "PASS", parameters = listOf("password")))
    }

}