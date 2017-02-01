package chat.willow.kale.irc.message.extension.monitor

import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class MonitorAddMessageTests {
    private lateinit var factory: MonitorAddMessage.Factory

    @Before fun setUp() {
        factory = MonitorAddMessage
    }

    @Test fun test_parse_SingleTarget() {
        val message = factory.parse(IrcMessage(command = "MONITOR", parameters = listOf("+", "target")))

        assertEquals(MonitorAddMessage(targets = listOf("target")), message)
    }

    @Test fun test_parse_MultipleTargets() {
        val message = factory.parse(IrcMessage(command = "MONITOR", parameters = listOf("+", "target1,target2")))

        assertEquals(MonitorAddMessage(targets = listOf("target1", "target2")), message)
    }

    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "MONITOR", parameters = listOf("+")))
        val messageTwo = factory.parse(IrcMessage(command = "MONITOR", parameters = listOf()))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_SingleTarget() {
        val message = factory.serialise(MonitorAddMessage(targets = listOf("target")))

        assertEquals(IrcMessage(command = "MONITOR", parameters = listOf("+", "target")), message)
    }

    @Test fun test_serialise_MultipleTargets() {
        val message = factory.serialise(MonitorAddMessage(targets = listOf("target1", "target2")))

        assertEquals(IrcMessage(command = "MONITOR", parameters = listOf("+", "target1,target2")), message)
    }
    
}