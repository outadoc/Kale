package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.message.rfc1459.PingMessage
import engineer.carrot.warren.kale.irc.message.rfc1459.PongMessage

object KaleRunner {
    @JvmStatic fun main(args: Array<String>) {
        println("Hello, Kale!")

        val kale = Kale().addDefaultMessages()
        val pingHandler = PingHandler()
        val pongHandler = PongHandler()

        kale.register(pongHandler)
        kale.register(pingHandler)

        kale.process("@tag;key=value :user!host@server WHATEVER :more stuff")
        kale.process("PING :token")
        kale.process("PONG :token2")
        kale.process("NICK nickname")
        kale.process("USER 1 2 3 4")
        kale.process("QUIT :")
        kale.process("JOIN #channel,#channel2 key1")
    }

    class PingHandler: IKaleHandler<PingMessage> {
        override val messageType = PingMessage::class.java

        override fun handle(messageOne: PingMessage) {
            println("handling ping message: $messageOne")
        }

    }

    class PongHandler: IKaleHandler<PongMessage> {
        override val messageType = PongMessage::class.java

        override fun handle(messageOne: PongMessage) {
            println("handling pong message: $messageOne")
        }

    }
}

