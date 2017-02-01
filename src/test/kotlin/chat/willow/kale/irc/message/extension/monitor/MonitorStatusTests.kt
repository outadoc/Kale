package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class MonitorStatusMessageTests {
    private lateinit var factory: MonitorStatusMessage.Factory

    @Before fun setUp() {
        factory = MonitorStatusMessage
    }

    @Test fun test_parse_SanityCheck() {
        val message = factory.parse(IrcMessage(command = "MONITOR", parameters = listOf("S")))

        assertEquals(MonitorStatusMessage(subCommand = "S"), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "MONITOR", parameters = listOf()))

        assertNull(messageOne)
    }

    @Test fun test_serialise_SanityCheck() {
        val message = factory.serialise(MonitorStatusMessage())

        assertEquals(IrcMessage(command = "MONITOR", parameters = listOf("S")), message)
    }
    
}