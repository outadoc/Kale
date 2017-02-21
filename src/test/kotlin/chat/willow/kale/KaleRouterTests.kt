package chat.willow.kale

import chat.willow.kale.irc.message.IMessage
import chat.willow.kale.irc.message.IMessageParser
import chat.willow.kale.irc.message.IMessageSerialiser
import chat.willow.kale.irc.message.IrcMessage
import chat.willow.kale.irc.message.extension.account_notify.AccountMessage
import chat.willow.kale.irc.message.extension.away_notify.AwayMessage
import chat.willow.kale.irc.message.extension.batch.BatchEndMessage
import chat.willow.kale.irc.message.extension.batch.BatchStartMessage
import chat.willow.kale.irc.message.extension.cap.*
import chat.willow.kale.irc.message.extension.chghost.ChgHostMessage
import chat.willow.kale.irc.message.extension.extended_join.ExtendedJoinMessage
import chat.willow.kale.irc.message.extension.sasl.AuthenticateMessage
import chat.willow.kale.irc.message.extension.sasl.Rpl903Message
import chat.willow.kale.irc.message.extension.sasl.Rpl904Message
import chat.willow.kale.irc.message.extension.sasl.Rpl905Message
import chat.willow.kale.irc.message.rfc1459.*
import chat.willow.kale.irc.message.rfc1459.rpl.*
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class KaleRouterTests {

    lateinit var sut: KaleRouter

    @Before fun setUp() {
        sut = KaleRouter()
    }

    @Test fun test_parserFor_AfterRegisteringCommand_ReturnsSameParser() {
        val parser = mock<IMessageParser<IMessage>>()
        val message = IrcMessage(command = "TEST")

        sut.routeCommandToParser(message.command, parser)

        val returnedParser = sut.parserFor(message)

        assertTrue(parser === returnedParser)
    }

    @Test fun test_parserFor_ForMissingCommand_ReturnsNull() {
        val parser = mock<IMessageParser<IMessage>>()
        val message = IrcMessage(command = "TEST")

        sut.routeCommandToParser(message.command, parser)

        val returnedParser = sut.parserFor(IrcMessage(command = "NOT TEST"))

        assertNull(returnedParser)
    }

    @Test fun test_parserFor_AfterRegisteringParserMatcher_ReturnsCorrectParser() {
        val parser = mock<IMessageParser<IMessage>>()
        val message = IrcMessage(command = "TEST")
        val matcher = object : ParserMatcher {
            override fun invoke(invokeMessage: IrcMessage): IMessageParser<*>? {
                return parser
            }
        }

        sut.routeCommandToParserMatcher(message.command, matcher)

        val returnedParser = sut.parserFor(message)

        assertTrue(parser === returnedParser)
    }

    @Test fun test_parserFor_AfterRegisteringParserMatcher_ForMissingCommand_ReturnsNull() {
        val parser = mock<IMessageParser<IMessage>>()
        val message = IrcMessage(command = "TEST")
        val matcher = object : ParserMatcher {
            override fun invoke(invokeMessage: IrcMessage): IMessageParser<*>? {
                return parser
            }
        }

        sut.routeCommandToParserMatcher(message.command, matcher)

        val returnedParser = sut.parserFor(IrcMessage(command = "NOT TEST"))

        assertNull(returnedParser)
    }

    @Test fun test_serialiserFor_AfterRoutingMessageToSerialiser_ReturnsCorrectSerialiser() {
        val serialiser = mock<IMessageSerialiser<IMessage>>()
        val message = mock<IMessage>()

        sut.routeMessageToSerialiser(message::class, serialiser)

        val returnedSerialiser = sut.serialiserFor(message::class.java)

        assertTrue(serialiser === returnedSerialiser)
    }

    @Test fun test_serialiserFor_AfterRoutingMessageToSerialiser_ForMissingClass_ReturnsNull() {
        val message = mock<IMessage>()

        val returnedSerialiser = sut.serialiserFor(message::class.java)

        assertNull(returnedSerialiser)
    }

    // Default message tests

    private fun assertParserAndSerialiserExist(command: String, messageClass: Class<*>) {
        val parser = sut.parserFor(IrcMessage(command = command))
        val serialiser = sut.serialiserFor(messageClass)

        assertNotNull(parser)
        assertNotNull(serialiser)
    }

    @Test fun test_useDefaults_Ping() {
        sut.useDefaults()

        assertParserAndSerialiserExist("PING", PingMessage::class.java)
    }

    @Test fun test_useDefaults_Pong() {
        sut.useDefaults()

        assertParserAndSerialiserExist("PONG", PongMessage::class.java)
    }

    @Test fun test_useDefaults_Nick() {
        sut.useDefaults()

        assertParserAndSerialiserExist("NICK", NickMessage::class.java)
    }

    @Test fun test_useDefaults_User() {
        sut.useDefaults()

        assertParserAndSerialiserExist("USER", UserMessage::class.java)
    }

    @Test fun test_useDefaults_Pass() {
        sut.useDefaults()

        assertParserAndSerialiserExist("PASS", PassMessage::class.java)
    }

    @Test fun test_useDefaults_Quit() {
        sut.useDefaults()

        assertParserAndSerialiserExist("QUIT", QuitMessage::class.java)
    }

    @Test fun test_useDefaults_Part() {
        sut.useDefaults()

        assertParserAndSerialiserExist("PART", PartMessage::class.java)
    }

    @Test fun test_useDefaults_Mode() {
        sut.useDefaults()

        assertParserAndSerialiserExist("MODE", ModeMessage::class.java)
    }

    @Test fun test_useDefaults_PrivMsg() {
        sut.useDefaults()

        assertParserAndSerialiserExist("PRIVMSG", PrivMsgMessage::class.java)
    }

    @Test fun test_useDefaults_Notice() {
        sut.useDefaults()

        assertParserAndSerialiserExist("NOTICE", NoticeMessage::class.java)
    }

    @Test fun test_useDefaults_Invite() {
        sut.useDefaults()

        assertParserAndSerialiserExist("INVITE", InviteMessage::class.java)
    }

    @Test fun test_useDefaults_Topic() {
        sut.useDefaults()

        assertParserAndSerialiserExist("TOPIC", TopicMessage::class.java)
    }

    @Test fun test_useDefaults_Kick() {
        sut.useDefaults()

        assertParserAndSerialiserExist("KICK", KickMessage::class.java)
    }

    @Test fun test_useDefaults_Join() {
        sut.useDefaults()

        val parserJoinOne = sut.parserFor(IrcMessage(command = "JOIN", parameters = listOf("")))
        val parserJoinTwo = sut.parserFor(IrcMessage(command = "JOIN", parameters = listOf("", "")))
        val parserExtendedJoin = sut.parserFor(IrcMessage(command = "JOIN", parameters = listOf("", "", "")))
        val parserTooManyParameters = sut.parserFor(IrcMessage(command = "JOIN", parameters = listOf("", "", "", "")))
        val parserTooFewParameters = sut.parserFor(IrcMessage(command = "JOIN", parameters = listOf()))

        val serialiserJoin = sut.serialiserFor(JoinMessage::class.java)
        val serialiserExtendedJoin = sut.serialiserFor(ExtendedJoinMessage::class.java)

        assertNotNull(parserJoinOne)
        assertNotNull(parserJoinTwo)
        assertNotNull(parserExtendedJoin)
        assertNull(parserTooManyParameters)
        assertNull(parserTooFewParameters)

        assertNotNull(serialiserJoin)
        assertNotNull(serialiserExtendedJoin)
    }

    @Test fun test_useDefaults_Cap() {
        sut.useDefaults()

        val parserAck = sut.parserFor(IrcMessage(command = "CAP", parameters = listOf("*", "ACK")))
        val parserEnd = sut.parserFor(IrcMessage(command = "CAP", parameters = listOf("*", "END")))
        val parserLs = sut.parserFor(IrcMessage(command = "CAP", parameters = listOf("*", "LS")))
        val parserNak = sut.parserFor(IrcMessage(command = "CAP", parameters = listOf("*", "NAK")))
        val parserReq = sut.parserFor(IrcMessage(command = "CAP", parameters = listOf("*", "REQ")))
        val parserNew = sut.parserFor(IrcMessage(command = "CAP", parameters = listOf("*", "NEW")))
        val parserDel = sut.parserFor(IrcMessage(command = "CAP", parameters = listOf("*", "DEL")))
        val parserUnknown = sut.parserFor(IrcMessage(command = "CAP", parameters = listOf("*", "UNK")))

        val serialiserAck = sut.serialiserFor(CapAckMessage::class.java)
        val serialiserEnd = sut.serialiserFor(CapEndMessage::class.java)
        val serialiserLs = sut.serialiserFor(CapLsMessage::class.java)
        val serialiserNak = sut.serialiserFor(CapNakMessage::class.java)
        val serialiserReq = sut.serialiserFor(CapReqMessage::class.java)
        val serialiserNew = sut.serialiserFor(CapNewMessage::class.java)
        val serialiserDel = sut.serialiserFor(CapDelMessage::class.java)

        assertNotNull(parserAck)
        assertNotNull(parserEnd)
        assertNotNull(parserLs)
        assertNotNull(parserNak)
        assertNotNull(parserReq)
        assertNotNull(parserNew)
        assertNotNull(parserDel)
        assertNull(parserUnknown)

        assertNotNull(serialiserAck)
        assertNotNull(serialiserEnd)
        assertNotNull(serialiserLs)
        assertNotNull(serialiserNak)
        assertNotNull(serialiserReq)
        assertNotNull(serialiserNew)
        assertNotNull(serialiserDel)
    }

    @Test fun test_useDefaults_Batch() {
        sut.useDefaults()

        val parserStart = sut.parserFor(IrcMessage(command = "BATCH", parameters = listOf("+")))
        val parserEnd = sut.parserFor(IrcMessage(command = "BATCH", parameters = listOf("-")))
        val parserUnknown = sut.parserFor(IrcMessage(command = "BATCH", parameters = listOf("*")))

        val serialiserStart = sut.serialiserFor(BatchStartMessage::class.java)
        val serialiserEnd = sut.serialiserFor(BatchEndMessage::class.java)
        
        assertNotNull(parserStart)
        assertNotNull(parserEnd)
        assertNull(parserUnknown)

        assertNotNull(serialiserStart)
        assertNotNull(serialiserEnd)
    }

    @Test fun test_useDefaults_Authenticate() {
        sut.useDefaults()

        assertParserAndSerialiserExist("AUTHENTICATE", AuthenticateMessage::class.java)
    }

    @Test fun test_useDefaults_Account() {
        sut.useDefaults()

        assertParserAndSerialiserExist("ACCOUNT", AccountMessage::class.java)
    }

    @Test fun test_useDefaults_Away() {
        sut.useDefaults()

        assertParserAndSerialiserExist("AWAY", AwayMessage::class.java)
    }

    @Test fun test_useDefaults_ChgHost() {
        sut.useDefaults()

        assertParserAndSerialiserExist("CHGHOST", ChgHostMessage::class.java)
    }

    @Test fun test_useDefaults_903() {
        sut.useDefaults()

        assertParserAndSerialiserExist("903", Rpl903Message::class.java)
    }

    @Test fun test_useDefaults_904() {
        sut.useDefaults()

        assertParserAndSerialiserExist("904", Rpl904Message::class.java)
    }

    @Test fun test_useDefaults_905() {
        sut.useDefaults()

        assertParserAndSerialiserExist("905", Rpl905Message::class.java)
    }

    @Test fun test_useDefaults_001() {
        sut.useDefaults()

        assertParserAndSerialiserExist("001", Rpl001Message::class.java)
    }

    @Test fun test_useDefaults_002() {
        sut.useDefaults()

        assertParserAndSerialiserExist("002", Rpl002Message::class.java)
    }

    @Test fun test_useDefaults_003() {
        sut.useDefaults()

        assertParserAndSerialiserExist("003", Rpl003Message::class.java)
    }

    @Test fun test_useDefaults_005() {
        sut.useDefaults()

        assertParserAndSerialiserExist("005", Rpl005Message::class.java)
    }

    @Test fun test_useDefaults_331() {
        sut.useDefaults()

        assertParserAndSerialiserExist("331", Rpl331Message::class.java)
    }

    @Test fun test_useDefaults_332() {
        sut.useDefaults()

        assertParserAndSerialiserExist("332", Rpl332Message::class.java)
    }

    @Test fun test_useDefaults_353() {
        sut.useDefaults()

        assertParserAndSerialiserExist("353", Rpl353Message::class.java)
    }

    @Test fun test_useDefaults_372() {
        sut.useDefaults()

        assertParserAndSerialiserExist("372", Rpl372Message::class.java)
    }

    @Test fun test_useDefaults_375() {
        sut.useDefaults()

        assertParserAndSerialiserExist("375", Rpl375Message::class.java)
    }

    @Test fun test_useDefaults_376() {
        sut.useDefaults()

        assertParserAndSerialiserExist("376", Rpl376Message::class.java)
    }

    @Test fun test_useDefaults_422() {
        sut.useDefaults()

        assertParserAndSerialiserExist("422", Rpl422Message::class.java)
    }

    @Test fun test_useDefaults_471() {
        sut.useDefaults()

        assertParserAndSerialiserExist("471", Rpl471Message::class.java)
    }

    @Test fun test_useDefaults_473() {
        sut.useDefaults()

        assertParserAndSerialiserExist("473", Rpl473Message::class.java)
    }

    @Test fun test_useDefaults_474() {
        sut.useDefaults()

        assertParserAndSerialiserExist("474", Rpl474Message::class.java)
    }

    @Test fun test_useDefaults_475() {
        sut.useDefaults()

        assertParserAndSerialiserExist("475", Rpl475Message::class.java)
    }

}