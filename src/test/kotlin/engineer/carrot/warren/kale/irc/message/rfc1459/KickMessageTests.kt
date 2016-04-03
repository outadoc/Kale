package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class KickMessageTests {
    lateinit var factory: IMessageFactory<KickMessage>

    @Before fun setUp() {
        factory = KickMessage.Factory
    }

    @Test fun test_parse_OneChannel_OneUser_NoComment() {
        val message = factory.parse(IrcMessage(command = "KICK", parameters = listOf("#channel1", "user1")))

        assertEquals(KickMessage(channels = listOf("#channel1"), users = listOf("user1")), message)
    }

    @Test fun test_parse_OneChannel_OneUser_NoComment_WithSource() {
        val message = factory.parse(IrcMessage(command = "KICK", prefix = "kicker", parameters = listOf("#channel1", "user1")))

        assertEquals(KickMessage(source = "kicker", channels = listOf("#channel1"), users = listOf("user1")), message)
    }
    
    @Test fun test_parse_OneChannel_OneUser_WithComment() {
        val message = factory.parse(IrcMessage(command = "KICK", parameters = listOf("#channel1", "user1", "kicked!")))

        assertEquals(KickMessage(channels = listOf("#channel1"), users = listOf("user1"), comment = "kicked!"), message)
    }
    
    @Test fun test_parse_MultipleChannels_MultipleUsers_NoComment() {
        val message = factory.parse(IrcMessage(command = "KICK", parameters = listOf("#channel1,#channel2,#channel3", "user1,user2,user3")))

        assertEquals(KickMessage(channels = listOf("#channel1", "#channel2", "#channel3"), users = listOf("user1", "user2", "user3")), message)
    }
    
    @Test fun test_parse_MultipleChannels_MultipleUsers_WithComment() {
        val message = factory.parse(IrcMessage(command = "KICK", parameters = listOf("#channel1,#channel2,#channel3", "user1,user2,user3", "kicked!!")))

        assertEquals(KickMessage(channels = listOf("#channel1", "#channel2", "#channel3"), users = listOf("user1", "user2", "user3"), comment = "kicked!!"), message)
    }
    
    @Test fun test_parse_MismatchedChannelsAndUsers() {
        val message = factory.parse(IrcMessage(command = "KICK", parameters = listOf("#channel1,#channel2", "user1,user2,user3")))

        assertNull(message)
    }
    
    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "KICK", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "KICK", parameters = listOf("#channel1,#channel2")))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_OneChannel_OneUser_NoComment_WithSource() {
        val message = factory.serialise(KickMessage(source = "kicker", channels = listOf("#channel1"), users = listOf("user1")))

        assertEquals(IrcMessage(prefix = "kicker", command = "KICK", parameters = listOf("#channel1", "user1")), message)
    }

    @Test fun test_serialise_OneChannel_OneUser_NoComment() {
        val message = factory.serialise(KickMessage(channels = listOf("#channel1"), users = listOf("user1")))

        assertEquals(IrcMessage(command = "KICK", parameters = listOf("#channel1", "user1")), message)
    }

    @Test fun test_serialise_OneChannel_OneUser_WithComment() {
        val message = factory.serialise(KickMessage(channels = listOf("#channel1"), users = listOf("user1"), comment = "kicked!!!"))

        assertEquals(IrcMessage(command = "KICK", parameters = listOf("#channel1", "user1", "kicked!!!")), message)
    }

    @Test fun test_serialise_MultipleChannels_MultipleUsers_NoComment() {
        val message = factory.serialise(KickMessage(channels = listOf("#channel1", "#channel2"), users = listOf("user1", "user2")))

        assertEquals(IrcMessage(command = "KICK", parameters = listOf("#channel1,#channel2", "user1,user2")), message)
    }

    @Test fun test_serialise_MultipleChannels_MultipleUsers_WithComment() {
        val message = factory.serialise(KickMessage(channels = listOf("#channel1", "#channel2"), users = listOf("user1", "user2"), comment = "kicked!!!"))

        assertEquals(IrcMessage(command = "KICK", parameters = listOf("#channel1,#channel2", "user1,user2", "kicked!!!")), message)
    }

    @Test fun test_serialise_MismatchedChannelsAndUsers() {
        val message = factory.serialise(KickMessage(channels = listOf("#channel1", "#channel2"), users = listOf("user1")))

        assertEquals(IrcMessage(command = "KICK", parameters = listOf("#channel1,#channel2", "user1")), message)
    }

}