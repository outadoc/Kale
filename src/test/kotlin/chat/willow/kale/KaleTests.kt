package chat.willow.kale

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class KaleTests {
    lateinit var kale: Kale
    lateinit var kaleRouter: IKaleRouter
    lateinit var mockHandlerOne: MockHandlerOne
    lateinit var mockHandlerSub: MockHandlerSubcommand

    @Before fun setUp() {
        // FIXME: use mock router instead
        kaleRouter = KaleRouter()

        mockHandlerOne = MockHandlerOne()
        mockHandlerSub = MockHandlerSubcommand()

        kale = Kale(kaleRouter)

        kaleRouter.routeCommandToParser("TEST1", MockMessageOne)
        kaleRouter.routeMessageToSerialiser(MockMessageOne::class, MockMessageOne)

        kaleRouter.routeCommandToParser("TEST", MockSubcommandMessage)

        kale.register(mockHandlerOne)
        kale.register(mockHandlerSub)
    }

    @Test fun test_init_firesNoHandlers() {
        assertEquals(0, mockHandlerOne.spyHandleMessageInvokations.size)
        assertEquals(0, mockHandlerSub.spyHandleMessageInvokations.size)
    }

    @Test fun test_command_firesTestHandler() {
        kale.process("TEST1 :token1")

        assertEquals(1, mockHandlerOne.spyHandleMessageInvokations.size)
        assertEquals(0, mockHandlerSub.spyHandleMessageInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandlerOne.spyHandleMessageInvokations[0])
    }

    @Test fun test_subcommand_firesTestHandler() {
        kale.process("TEST * SUB :token1")

        assertEquals(1, mockHandlerSub.spyHandleMessageInvokations.size)
        assertEquals(0, mockHandlerOne.spyHandleMessageInvokations.size)
        assertEquals(MockSubcommandMessage(mockToken = "token1"), mockHandlerSub.spyHandleMessageInvokations[0])
    }

    @Test fun test_multipleCommands_firesTestHandlerMultipleTimes() {
        kale.process("TEST1 :token1")
        kale.process("TEST2 :token1")
        kale.process("TEST1 :token2")

        assertEquals(2, mockHandlerOne.spyHandleMessageInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandlerOne.spyHandleMessageInvokations[0])
        assertEquals(MockMessageOne(mockToken = "token2"), mockHandlerOne.spyHandleMessageInvokations[1])
    }

    @Test fun test_multipleCommands_orderPreserved() {
        kale.process("TEST1 :token1")
        kale.process("TEST1 :token2")
        kale.process("TEST1 :token3")

        assertEquals(3, mockHandlerOne.spyHandleMessageInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandlerOne.spyHandleMessageInvokations[0])
        assertEquals(MockMessageOne(mockToken = "token2"), mockHandlerOne.spyHandleMessageInvokations[1])
        assertEquals(MockMessageOne(mockToken = "token3"), mockHandlerOne.spyHandleMessageInvokations[2])
    }

    @Test fun test_command_firesTestHandler_NoTags_ExpectNoTagsPassedToHandler() {
        kale.process("TEST1 :token1")

        assertEquals(1, mockHandlerOne.spyHandleMessageInvokations.size)
        assertEquals(0, mockHandlerSub.spyHandleMessageInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandlerOne.spyHandleMessageInvokations[0])
        assertEquals(mapOf<String, String?>(), mockHandlerOne.spyHandleTagsInvokations[0])
    }

    @Test fun test_command_firesTestHandler_HasTags_ExpectTagsPassedToHandler() {
        kale.process("@tag1;tag2=2 TEST1 :token1")

        assertEquals(1, mockHandlerOne.spyHandleMessageInvokations.size)
        assertEquals(0, mockHandlerSub.spyHandleMessageInvokations.size)
        assertEquals(MockMessageOne(mockToken = "token1"), mockHandlerOne.spyHandleMessageInvokations[0])
        assertEquals(mapOf("tag1" to null, "tag2" to "2"), mockHandlerOne.spyHandleTagsInvokations[0])
    }

    @Test fun test_subcommand_firesTestHandler_NoTags_ExpectNoTagsPassedToHandler() {
        kale.process("TEST * SUB :token1")

        assertEquals(1, mockHandlerSub.spyHandleMessageInvokations.size)
        assertEquals(0, mockHandlerOne.spyHandleMessageInvokations.size)
        assertEquals(MockSubcommandMessage(mockToken = "token1"), mockHandlerSub.spyHandleMessageInvokations[0])
        assertEquals(mapOf<String, String?>(), mockHandlerSub.spyHandleTagsInvokations[0])
    }


    @Test fun test_subcommand_firesTestHandler_HasTags_ExpectTagsPassedToHandler() {
        kale.process("@tag3;tag4=4 TEST * SUB :token1")

        assertEquals(1, mockHandlerSub.spyHandleMessageInvokations.size)
        assertEquals(0, mockHandlerOne.spyHandleMessageInvokations.size)
        assertEquals(MockSubcommandMessage(mockToken = "token1"), mockHandlerSub.spyHandleMessageInvokations[0])
        assertEquals(mapOf("tag3" to null, "tag4" to "4"), mockHandlerSub.spyHandleTagsInvokations[0])
    }


    @Test fun test_serialise_SanityCheck() {
        val message = kale.serialise(MockMessageOne(mockToken = "TestToken1"))

        assertEquals(message, IrcMessage(command = "TEST1", parameters = listOf("TestToken1")))
    }

    @Test fun test_serialise_UnknownMessage() {
        val message = kale.serialise(MockUnknownMessage(unknownParameter = "UnknownParameter1"))

        assertEquals(message, null)
    }

    @Test fun test_unregister_RemovesCorrectHandler() {
        kale.unregister(mockHandlerOne)

        kale.process("TEST1 :token")
        kale.process("TEST * SUB :token")

        assertEquals(0, mockHandlerOne.spyHandleMessageInvokations.size)
        assertEquals(1, mockHandlerSub.spyHandleMessageInvokations.size)
    }

    class MockHandlerOne : IKaleHandler<MockMessageOne> {
        var spyHandleMessageInvokations: List<MockMessageOne> = mutableListOf()
        var spyHandleTagsInvokations: List<Map<String, String?>> = mutableListOf()

        override val messageType = MockMessageOne::class.java

        override fun handle(message: MockMessageOne, tags: Map<String, String?>) {
            spyHandleMessageInvokations += message
            spyHandleTagsInvokations += tags
        }
    }

    data class MockMessageOne(val mockToken: String): IMessage {
        override val command: String = "TEST1"

        companion object Factory: IMessageParser<MockMessageOne>, IMessageSerialiser<MockMessageOne> {
            override fun parse(message: IrcMessage): MockMessageOne? {
                return MockMessageOne(message.parameters[0])
            }

            override fun serialise(message: MockMessageOne): IrcMessage? {
                return IrcMessage(command = message.command, parameters = listOf(message.mockToken))
            }
        }
    }

    class MockHandlerSubcommand : IKaleHandler<MockSubcommandMessage> {
        var spyHandleMessageInvokations: List<MockSubcommandMessage> = mutableListOf()
        var spyHandleTagsInvokations: List<Map<String, String?>> = mutableListOf()

        override val messageType = MockSubcommandMessage::class.java

        override fun handle(message: MockSubcommandMessage, tags: Map<String, String?>) {
            spyHandleMessageInvokations += message
            spyHandleTagsInvokations += tags
        }
    }

    data class MockSubcommandMessage(val mockToken: String): IMessage {
        override val command: String = "TEST"

        companion object Factory: IMessageParser<MockSubcommandMessage>, IMessageSerialiser<MockSubcommandMessage> {
            override fun parse(message: IrcMessage): MockSubcommandMessage? {
                return MockSubcommandMessage(message.parameters[2])
            }

            override fun serialise(message: MockSubcommandMessage): IrcMessage? {
                return IrcMessage(command = message.command, parameters = listOf("*", "SUB", message.mockToken))
            }
        }
    }

    data class MockUnknownMessage(val unknownParameter: String): IMessage {
        override val command: String = "UNKNOWN"
    }
}