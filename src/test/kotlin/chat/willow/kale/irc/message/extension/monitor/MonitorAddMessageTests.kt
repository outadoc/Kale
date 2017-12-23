package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.core.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class MonitorRemoveMessageTests {

    private lateinit var messageParser: MonitorMessage.Remove.Command.Parser
    private lateinit var messageSerialiser: MonitorMessage.Remove.Command.Serialiser

    @Before fun setUp() {
        messageParser = MonitorMessage.Remove.Command.Parser
        messageSerialiser = MonitorMessage.Remove.Command.Serialiser
    }

    @Test fun test_parse_SingleTarget() {
        val message = messageParser.parse(IrcMessage(command = "MONITOR", parameters = listOf("-", "target")))

        assertEquals(MonitorMessage.Remove.Command(targets = listOf("target")), message)
    }

    @Test fun test_parse_MultipleTargets() {
        val message = messageParser.parse(IrcMessage(command = "MONITOR", parameters = listOf("-", "target1,target2")))

        assertEquals(MonitorMessage.Remove.Command(targets = listOf("target1", "target2")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = messageParser.parse(IrcMessage(command = "MONITOR", parameters = listOf("+")))
        val messageTwo = messageParser.parse(IrcMessage(command = "MONITOR", parameters = listOf()))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_SingleTarget() {
        val message = messageSerialiser.serialise(MonitorMessage.Remove.Command(targets = listOf("target")))

        assertEquals(IrcMessage(command = "MONITOR", parameters = listOf("-", "target")), message)
    }

    @Test fun test_serialise_MultipleTargets() {
        val message = messageSerialiser.serialise(MonitorMessage.Remove.Command(targets = listOf("target1", "target2")))

        assertEquals(IrcMessage(command = "MONITOR", parameters = listOf("-", "target1,target2")), message)
    }

}