package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.core.message.IrcMessage
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class MonitorClearMessageTests {

    private lateinit var messageParser: MonitorMessage.Clear.Command.Parser
    private lateinit var messageSerialiser: MonitorMessage.Clear.Command.Serialiser

    @BeforeTest fun setUp() {
        messageParser = MonitorMessage.Clear.Command.Parser
        messageSerialiser = MonitorMessage.Clear.Command.Serialiser
    }

    @Test fun test_parse_SanityCheck() {
        val message = messageParser.parse(IrcMessage(command = "MONITOR", parameters = listOf("C")))

        assertEquals(MonitorMessage.Clear.Command, message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "MONITOR", parameters = listOf()))

        assertNull(messageOne)
    }

    @Test fun test_serialise_SanityCheck() {
        val message = messageSerialiser.serialise(MonitorMessage.Clear.Command)

        assertEquals(IrcMessage(command = "MONITOR", parameters = listOf("C")), message)
    }
    
}