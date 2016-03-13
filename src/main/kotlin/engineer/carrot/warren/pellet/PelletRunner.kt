package engineer.carrot.warren.pellet

import engineer.carrot.warren.pellet.irc.message.rfc1459.PingMessage
import engineer.carrot.warren.pellet.irc.message.rfc1459.PongMessage

object PelletRunner {
    @JvmStatic fun main(args: Array<String>) {
        println("Hello, Pellet!")

        val pellet = Pellet().addDefaultMessages()
        val pingHandler = PingHandler()
        val pongHandler = PongHandler()

        pellet.register(pongHandler)
        pellet.register(pingHandler)

        pellet.process("@tag;key=value :user!host@server WHATEVER :more stuff")
        pellet.process("PING :token")
        pellet.process("PONG :token2")
        pellet.process("NICK nickname")
        pellet.process("USER 1 2 3 4")
        pellet.process("QUIT :")
    }

    class PingHandler: IPelletHandler<PingMessage> {
        override val messageType = PingMessage::class.java

        override fun handle(messageOne: PingMessage) {
            println("handling ping message: $messageOne")
        }

    }

    class PongHandler: IPelletHandler<PongMessage> {
        override val messageType = PongMessage::class.java

        override fun handle(messageOne: PongMessage) {
            println("handling pong message: $messageOne")
        }

    }
}

