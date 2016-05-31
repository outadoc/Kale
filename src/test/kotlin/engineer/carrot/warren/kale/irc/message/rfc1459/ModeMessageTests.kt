package engineer.carrot.warren.kale.irc.message.rfc1459

import engineer.carrot.warren.kale.IKaleParsingStateDelegate
import engineer.carrot.warren.kale.irc.message.IrcMessage
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ModeMessageTests {
    lateinit var factory: ModeMessage.Factory

    @Before fun setUp() {
        val modeFactory = ModeMessage.Factory
        modeFactory.parsingStateDelegate = null

        factory = modeFactory
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

    @Test fun test_parse_ChannelMode_UsingStateDelegate_ParsesWithParameter() {
        factory.parsingStateDelegate = object : IKaleParsingStateDelegate {
            override fun modeTakesAParameter(isAdding: Boolean, token: Char): Boolean {
                return token == 'x'
            }

        }

        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("#Channel", "+x", "a parameter")))

        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'x', parameter = "a parameter")

        assertEquals(ModeMessage(target = "#Channel", modifiers = listOf(firstModifier)), message)
    }

    @Test fun test_parse_ChannelMode_UsingStateDelegate_ParsesWithoutParameter() {
        factory.parsingStateDelegate = object : IKaleParsingStateDelegate {
            override fun modeTakesAParameter(isAdding: Boolean, token: Char): Boolean {
                return token != 'x'
            }

        }

        val message = factory.parse(IrcMessage(command = "MODE", parameters = listOf("#Channel", "-x", "extraneous parameter")))

        val firstModifier = ModeMessage.ModeModifier(type = '-', mode = 'x', parameter = null)

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

    @Test fun test_serialise_AddingSingleMode() {
        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'A')

        val message = factory.serialise(ModeMessage(target = "#target", modifiers = listOf(firstModifier)))

        assertEquals(IrcMessage(command = "MODE", parameters = listOf("#target", "+A")), message)
    }

    @Test fun test_serialise_RemovingSingleMode() {
        val firstModifier = ModeMessage.ModeModifier(type = '-', mode = 'A')

        val message = factory.serialise(ModeMessage(target = "#target", modifiers = listOf(firstModifier)))

        assertEquals(IrcMessage(command = "MODE", parameters = listOf("#target", "-A")), message)
    }

    @Test fun test_serialise_AddingMultipleModes_SingleParameter() {
        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'i')
        val secondModifier = ModeMessage.ModeModifier(type = '+', mode = 'm')
        val thirdModifier = ModeMessage.ModeModifier(type = '+', mode = 'I', parameter = "*!*@*.fi")

        val message = factory.serialise(ModeMessage(target = "#Finnish", modifiers = listOf(firstModifier, secondModifier, thirdModifier)))

        assertEquals(IrcMessage(command = "MODE", parameters = listOf("#Finnish", "+i", "+m", "+I", "*!*@*.fi")), message)
    }

    @Test fun test_serialise_AddingModesSeparately_SingleParameterEach() {
        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'b', parameter = "*!*@*.edu")
        val secondModifier = ModeMessage.ModeModifier(type = '+', mode = 'e', parameter = "*!*@*.bu.edu")
        
        val message = factory.serialise(ModeMessage(target = "&oulu", modifiers = listOf(firstModifier, secondModifier)))
        
        assertEquals(IrcMessage(command = "MODE", parameters = listOf("&oulu", "+b", "*!*@*.edu", "+e", "*!*@*.bu.edu")), message)
    }

    @Test fun test_serialise_SettingChannelKeyExample() {
        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'k', parameter = "oulu")

        val message = factory.serialise(ModeMessage(target = "#42", modifiers = listOf(firstModifier)))

        assertEquals(IrcMessage(command = "MODE", parameters = listOf("#42", "+k", "oulu")), message)
    }

    @Test fun test_serialise_AddingAndRemovingModes_InOneChunk() {
        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'o', parameter = "nick1")
        val secondModifier = ModeMessage.ModeModifier(type = '-', mode = 'o', parameter = "nick2")

        val message = factory.serialise(ModeMessage(target = "#Channel", modifiers = listOf(firstModifier, secondModifier)))

        assertEquals(IrcMessage(command = "MODE", parameters = listOf("#Channel", "+o", "nick1", "-o", "nick2")), message)
    }

    @Test fun test_serialise_ListingExceptionMasksExample() {
        val firstModifier = ModeMessage.ModeModifier(mode = 'e')

        val message = factory.serialise(ModeMessage(target = "#Channel", modifiers = listOf(firstModifier)))

        assertEquals(IrcMessage(command = "MODE", parameters = listOf("#Channel", "e")), message)
    }

    @Test fun test_serialise_UserMode_TurnOffWallOpsExample() {
        val firstModifier = ModeMessage.ModeModifier(type = '-', mode = 'w')

        val message = factory.serialise(ModeMessage(target = "Wiz", modifiers = listOf(firstModifier)))

        assertEquals(IrcMessage(command = "MODE", parameters = listOf("Wiz", "-w")), message)
    }

    @Test fun test_serialise_UserMode_MakeInvisibleExample() {
        val firstModifier = ModeMessage.ModeModifier(type = '+', mode = 'i')

        val message = factory.serialise(ModeMessage(target = "Angel", modifiers = listOf(firstModifier)))

        assertEquals(IrcMessage(command = "MODE", parameters = listOf("Angel", "+i")), message)
    }
    
}