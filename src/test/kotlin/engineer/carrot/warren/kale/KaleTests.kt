package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.message.IMessage
import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.mockito.runners.MockitoJUnitRunner

class KaleTests {
    lateinit var kale: Kale
    lateinit var mockHandler: MockHandler

    @Before fun setUp() {
        mockHandler = MockHandler()

        kale = Kale()

        kale.addMessageFromFactory(MockMessageOne.Factory)

        kale.register(mockHandler)
    }

    @Test fun test_init_firesNoHandlers() {
        assertEquals(0, mockHandler.spyHandleInvokations.size)
    }

    @Test fun test_command_firesTestHandler() {
        kale.process("TEST1 :token1")

        assertEquals(1, mockHandler.spyHandleInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandler.spyHandleInvokations[0])
    }

    @Test fun test_multipleCommands_firesTestHandlerMultipleTimes() {
        kale.process("TEST1 :token1")
        kale.process("TEST2 :token1")
        kale.process("TEST1 :token2")

        assertEquals(2, mockHandler.spyHandleInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandler.spyHandleInvokations[0])
        assertEquals(MockMessageOne(mockToken = "token2"), mockHandler.spyHandleInvokations[1])
    }

    @Test fun test_multipleCommands_orderPreserved() {
        kale.process("TEST1 :token1")
        kale.process("TEST1 :token2")
        kale.process("TEST1 :token3")

        assertEquals(3, mockHandler.spyHandleInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandler.spyHandleInvokations[0])
        assertEquals(MockMessageOne(mockToken = "token2"), mockHandler.spyHandleInvokations[1])
        assertEquals(MockMessageOne(mockToken = "token3"), mockHandler.spyHandleInvokations[2])
    }

    class MockHandler: IKaleHandler<MockMessageOne> {
        var spyHandleInvokations: List<MockMessageOne> = mutableListOf()

        override val messageType = MockMessageOne::class.java

        override fun handle(messageOne: MockMessageOne) {
            spyHandleInvokations += messageOne
        }
    }

    data class MockMessageOne(val mockToken: String): IMessage {
        companion object Factory: IMessageFactory<MockMessageOne> {
            override val messageType = MockMessageOne::class.java
            override val command = "TEST1"

            override fun parse(message: IrcMessage): MockMessageOne? {
                return MockMessageOne(message.parameters[0])
            }

            override fun serialise(message: MockMessageOne): IrcMessage? {
                return IrcMessage(command = command, parameters = listOf(message.mockToken))
            }
        }
    }
}