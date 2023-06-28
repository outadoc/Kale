package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.core.message.IrcMessage
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test

class MonitorListMessageTests {

    private lateinit var messageParser: MonitorMessage.ListAll.Command.Parser
    private lateinit var messageSerialiser: MonitorMessage.ListAll.Command.Serialiser

    @BeforeTest fun setUp() {
        messageParser = MonitorMessage.ListAll.Command.Parser
        messageSerialiser = MonitorMessage.ListAll.Command.Serialiser
    }

    @Test fun test_parse_SanityCheck() {
        val message = messageParser.parse(IrcMessage(command = "MONITOR", parameters = listOf("L")))

        assertEquals(MonitorMessage.ListAll.Command, message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "MONITOR", parameters = listOf()))

        assertNull(messageOne)
    }

    @Test fun test_serialise_SanityCheck() {
        val message = messageSerialiser.serialise(MonitorMessage.ListAll.Command)

        assertEquals(IrcMessage(command = "MONITOR", parameters = listOf("L")), message)
    }
    
}