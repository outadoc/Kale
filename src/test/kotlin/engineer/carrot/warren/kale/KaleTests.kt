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
    lateinit var mockHandlerOne: MockHandlerOne
    lateinit var mockHandlerSub: MockHandlerSubcommand

    @Before fun setUp() {
        mockHandlerOne = MockHandlerOne()
        mockHandlerSub = MockHandlerSubcommand()

        kale = Kale()

        kale.addMessageFromFactory(MockMessageOne.Factory)
        kale.addMessageFromFactory(MockSubcommandMessage.Factory)

        kale.register(mockHandlerOne)
        kale.register(mockHandlerSub)
    }

    @Test fun test_init_firesNoHandlers() {
        assertEquals(0, mockHandlerOne.spyHandleInvokations.size)
        assertEquals(0, mockHandlerSub.spyHandleInvokations.size)
    }

    @Test fun test_command_firesTestHandler() {
        kale.process("TEST1 :token1")

        assertEquals(1, mockHandlerOne.spyHandleInvokations.size)
        assertEquals(0, mockHandlerSub.spyHandleInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandlerOne.spyHandleInvokations[0])
    }
    
    @Test fun test_subcommand_firesTestHandler() {
        kale.process("TEST * SUB :token1")

        assertEquals(1, mockHandlerSub.spyHandleInvokations.size)
        assertEquals(0, mockHandlerOne.spyHandleInvokations.size)
        assertEquals(MockSubcommandMessage(mockToken = "token1"), mockHandlerSub.spyHandleInvokations[0])
    }

    @Test fun test_multipleCommands_firesTestHandlerMultipleTimes() {
        kale.process("TEST1 :token1")
        kale.process("TEST2 :token1")
        kale.process("TEST1 :token2")

        assertEquals(2, mockHandlerOne.spyHandleInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandlerOne.spyHandleInvokations[0])
        assertEquals(MockMessageOne(mockToken = "token2"), mockHandlerOne.spyHandleInvokations[1])
    }

    @Test fun test_multipleCommands_orderPreserved() {
        kale.process("TEST1 :token1")
        kale.process("TEST1 :token2")
        kale.process("TEST1 :token3")

        assertEquals(3, mockHandlerOne.spyHandleInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandlerOne.spyHandleInvokations[0])
        assertEquals(MockMessageOne(mockToken = "token2"), mockHandlerOne.spyHandleInvokations[1])
        assertEquals(MockMessageOne(mockToken = "token3"), mockHandlerOne.spyHandleInvokations[2])
    }

    @Test fun test_serialise_SanityCheck() {
        val message = kale.serialise(MockMessageOne(mockToken = "TestToken1"))

        assertEquals(message, IrcMessage(command = "TEST1", parameters = listOf("TestToken1")))
    }

    @Test fun test_serialise_UnknownMessage() {
        val message = kale.serialise(MockUnknownMessage(unknownParameter = "UnknownParameter1"))

        assertEquals(message, null)
    }

    class MockHandlerOne : IKaleHandler<MockMessageOne> {
        var spyHandleInvokations: List<MockMessageOne> = mutableListOf()

        override val messageType = MockMessageOne::class.java

        override fun handle(message: MockMessageOne) {
            spyHandleInvokations += message
        }
    }

    data class MockMessageOne(val mockToken: String): IMessage {
        companion object Factory: IMessageFactory<MockMessageOne> {
            override val messageType = MockMessageOne::class.java
            override val key = "TEST1"

            override fun parse(message: IrcMessage): MockMessageOne? {
                return MockMessageOne(message.parameters[0])
            }

            override fun serialise(message: MockMessageOne): IrcMessage? {
                return IrcMessage(command = key, parameters = listOf(message.mockToken))
            }
        }
    }

    class MockHandlerSubcommand : IKaleHandler<MockSubcommandMessage> {
        var spyHandleInvokations: List<MockSubcommandMessage> = mutableListOf()

        override val messageType = MockSubcommandMessage::class.java

        override fun handle(message: MockSubcommandMessage) {
            spyHandleInvokations += message
        }
    }

    data class MockSubcommandMessage(val mockToken: String): IMessage {
        companion object Factory: IMessageFactory<MockSubcommandMessage> {
            override val messageType = MockSubcommandMessage::class.java
            override val key = "TESTSUB"

            override fun parse(message: IrcMessage): MockSubcommandMessage? {
                return MockSubcommandMessage(message.parameters[2])
            }

            override fun serialise(message: MockSubcommandMessage): IrcMessage? {
                return IrcMessage(command = key, parameters = listOf("*", "SUB", message.mockToken))
            }
        }
    }

    data class MockUnknownMessage(val unknownParameter: String): IMessage { }
}