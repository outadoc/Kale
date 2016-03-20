package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.irc.message.IMessageFactory
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class ModeMessageTests {
    lateinit var factory: IMessageFactory<ModeMessage>

    @Before fun setUp() {
        factory = ModeMessage.Factory
    }

    @Test fun test_parse_AddingSingleMode() {
        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("#Channel", "+A")))

        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'A')

        assertEquals(ModeMessage(target = "#Channel", modifiers = listOf(firstModifier)), message)
    }

    @Test fun test_parse_RemovingSingleMode() {
        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("#Channel", "-A")))

        val firstModifier = ModeMessage.ModeModifier(type = '-', mode = 'A')

        assertEquals(ModeMessage(target = "#Channel", modifiers = listOf(firstModifier)), message)
    }

    @Test fun test_parse_AddingMultipleModes_SingleParameter() {
        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("#Finnish", "+imI", "*!*@*.fi")))

        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'i')
        val secondModifier = ModeMessage.ModeModifier(type = '+', mode = 'm')
        val thirdModifier = ModeMessage.ModeModifier(type = '+', mode = 'I', parameter = "*!*@*.fi")

        assertEquals(ModeMessage(target = "#Finnish", modifiers = listOf(firstModifier, secondModifier, thirdModifier)), message)
    }

    @Test fun test_parse_AddingModesSeparately_SingleParameterEach() {
        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("&oulu", "+b", "*!*@*.edu", "+e", "*!*@*.bu.edu")))

        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'b', parameter = "*!*@*.edu")
        val secondModifier = ModeMessage.ModeModifier(type = '+', mode = 'e', parameter = "*!*@*.bu.edu")

        assertEquals(ModeMessage(target = "&oulu", modifiers = listOf(firstModifier, secondModifier)), message)
    }

    @Test fun test_parse_SettingChannelKeyExample() {
        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("#42", "+k", "oulu")))

        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'k', parameter = "oulu")

        assertEquals(ModeMessage(target = "#42", modifiers = listOf(firstModifier)), message)
    }

    @Test fun test_parse_AddingAndRemovingModes_InOneChunk() {
        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("#Channel", "+o-o", "nick1", "nick2")))

        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'o', parameter = "nick1")
        val secondModifier = ModeMessage.ModeModifier(type = '-', mode = 'o', parameter = "nick2")

        assertEquals(ModeMessage(target = "#Channel", modifiers = listOf(firstModifier, secondModifier)), message)
    }

    @Test fun test_parse_ListingExceptionMasksExample() {
        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("#Channel", "e")))

        val firstModifier = ModeMessage.ModeModifier(mode = 'e')

        assertEquals(ModeMessage(target = "#Channel", modifiers = listOf(firstModifier)), message)
    }

    @Test fun test_parse_UserMode_TurnOffWallOpsExample() {
        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("Wiz", "-w")))

        val firstModifier = ModeMessage.ModeModifier(type = '-', mode = 'w')

        assertEquals(ModeMessage(target = "Wiz", modifiers = listOf(firstModifier)), message)
    }

    @Test fun test_parse_UserMode_MakeInvisibleExample() {
        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("Angel", "+i")))

        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'i')

        assertEquals(ModeMessage(target = "Angel", modifiers = listOf(firstModifier)), message)
    }

    @Test fun test_parse_NoParameters() {
        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf()))

        assertEquals(null, message)
    }

    @Test fun test_parse_OneParameter() {
        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("parameter")))

        assertEquals(null, message)
    }
}