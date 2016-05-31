package engineer.carrot.warren.kale.irc.message.rpl

import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class Rpl005MessageTests {
    lateinit var factory: Rpl005Message.Factory

    @Before fun setUp() {
        factory = Rpl005Message.Factory
    }

    @Test fun test_parse_SingleToken_NoValue() {
        val message = factory.parse(IrcMessage(command = "005", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "KEY")))

        assertEquals(Rpl005Message(source = "imaginary.bunnies.io", target = "test-nickname", tokens = mapOf("KEY" to null)), message)
    }

    @Test fun test_parse_SingleToken_NoValue_ButEqualsPresent() {
        val message = factory.parse(IrcMessage(command = "005", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "KEY=")))

        assertEquals(Rpl005Message(source = "imaginary.bunnies.io", target = "test-nickname", tokens = mapOf("KEY" to null)), message)
    }

    @Test fun test_parse_SingleToken_WithValue() {
        val message = factory.parse(IrcMessage(command = "005", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "KEY=VALUE")))

        assertEquals(Rpl005Message(source = "imaginary.bunnies.io", target = "test-nickname", tokens = mapOf("KEY" to "VALUE")), message)
    }

    @Test fun test_parse_MultipleTokens_MultipleTypesOfValues() {
        val message = factory.parse(IrcMessage(command = "005", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "KEY=VALUE", "KEY2", "KEY3=", "KEY4=\uD83D\uDC30")))

        assertEquals(Rpl005Message(source = "imaginary.bunnies.io", target = "test-nickname", tokens = mapOf("KEY" to "VALUE", "KEY2" to null, "KEY3" to null, "KEY4" to "\uD83D\uDC30")), message)
    }
    
    @Test fun test_parse_TooFewParameters() {
        val messageOne = factory.parse(IrcMessage(command = "005", parameters = listOf()))
        val messageTwo = factory.parse(IrcMessage(command = "005", parameters = listOf("test-nickname2")))

        assertNull(messageOne)
        assertNull(messageTwo)
    }

    @Test fun test_serialise_SingleToken_NoValue() {
        val message = factory.serialise(Rpl005Message(source = "imaginary.bunnies.io", target = "test-nickname", tokens = mapOf("KEY" to null)))

        assertEquals(IrcMessage(command = "005", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "KEY")), message)
    }

    @Test fun test_serialise_SingleToken_NoValue_ButEqualsPresent() {
        val message = factory.serialise(Rpl005Message(source = "imaginary.bunnies.io", target = "test-nickname", tokens = mapOf("KEY" to null)))

        assertEquals(IrcMessage(command = "005", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "KEY")), message)
    }

    @Test fun test_serialise_SingleToken_WithValue() {
        val message = factory.serialise(Rpl005Message(source = "imaginary.bunnies.io", target = "test-nickname", tokens = mapOf("KEY" to "VALUE")))

        assertEquals(IrcMessage(command = "005", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "KEY=VALUE")), message)
    }

    @Test fun test_serialise_MultipleTokens_MultipleTypesOfValues() {
        val message = factory.serialise(Rpl005Message(source = "imaginary.bunnies.io", target = "test-nickname", tokens = mapOf("KEY" to "VALUE", "KEY2" to null, "KEY3" to null, "KEY4" to "\uD83D\uDC30")))

        assertEquals(IrcMessage(command = "005", prefix = "imaginary.bunnies.io", parameters = listOf("test-nickname", "KEY=VALUE", "KEY2", "KEY3", "KEY4=\uD83D\uDC30")), message)
    }
    
}