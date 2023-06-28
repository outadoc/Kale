package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.core.message.IrcMessage
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class MonitorStatusMessageTests {

    private lateinit var messageParser: MonitorMessage.Status.Command.Parser
    private lateinit var messageSerialiser: MonitorMessage.Status.Command.Serialiser

    @BeforeTest fun setUp() {
        messageParser = MonitorMessage.Status.Command.Parser
        messageSerialiser = MonitorMessage.Status.Command.Serialiser
    }

    @Test fun test_parse_SanityCheck() {
        val message = messageParser.parse(IrcMessage(command = "MONITOR", parameters = listOf("S")))

        assertEquals(MonitorMessage.Status.Command, message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "MONITOR", parameters = listOf()))

        assertNull(messageOne)
    }

    @Test fun test_serialise_SanityCheck() {
        val message = messageSerialiser.serialise(MonitorMessage.Status.Command)

        assertEquals(IrcMessage(command = "MONITOR", parameters = listOf("S")), message)
    }
    
}