package engineer.carrot.warren.pellet

import engineer.carrot.warren.pellet.irc.message.IMessage
import engineer.carrot.warren.pellet.irc.message.IMessageFactory
import engineer.carrot.warren.pellet.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.mockito.runners.MockitoJUnitRunner

class PelletTests {
    lateinit var pellet: Pellet
    lateinit var mockHandler: MockHandler

    @Before fun setUp() {
        mockHandler = MockHandler()

        pellet = Pellet()

        pellet.addMessageFromFactory(MockMessageOne.Factory)

        pellet.register(mockHandler)
    }

    @Test fun test_init_firesNoHandlers() {
        assertEquals(0, mockHandler.spyHandleInvokations.size)
    }

    @Test fun test_command_firesTestHandler() {
        pellet.process("TEST1 :token1")

        assertEquals(1, mockHandler.spyHandleInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandler.spyHandleInvokations[0])
    }

    @Test fun test_multipleCommands_firesTestHandlerMultipleTimes() {
        pellet.process("TEST1 :token1")
        pellet.process("TEST2 :token1")
        pellet.process("TEST1 :token2")

        assertEquals(2, mockHandler.spyHandleInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandler.spyHandleInvokations[0])
        assertEquals(MockMessageOne(mockToken = "token2"), mockHandler.spyHandleInvokations[1])
    }

    @Test fun test_multipleCommands_orderPreserved() {
        pellet.process("TEST1 :token1")
        pellet.process("TEST1 :token2")
        pellet.process("TEST1 :token3")

        assertEquals(3, mockHandler.spyHandleInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandler.spyHandleInvokations[0])
        assertEquals(MockMessageOne(mockToken = "token2"), mockHandler.spyHandleInvokations[1])
        assertEquals(MockMessageOne(mockToken = "token3"), mockHandler.spyHandleInvokations[2])
    }

    class MockHandler: IPelletHandler<MockMessageOne> {
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

            override fun serialise(messageOne: MockMessageOne): IrcMessage? {
                return IrcMessage(command = command, parameters = listOf(messageOne.mockToken))
            }
        }
    }
}