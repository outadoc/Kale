package engineer.carrot.warren.kale

import engineer.carrot.warren.kale.irc.message.rfc1459.*

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
        kale.process("PART #channel,#channel2")

        println(kale.serialise(PingMessage(token = "token")))
        println(kale.serialise(PongMessage(token = "token2")))
        println(kale.serialise(NickMessage(nickname = "nickname")))
        println(kale.serialise(UserMessage(username = "1", hostname = "2", servername = "3", realname = "4")))
        println(kale.serialise(QuitMessage(message = "")))
        println(kale.serialise(JoinMessage(channels = listOf("#channel", "#channel2"), keys = listOf("key1"))))
        println(kale.serialise(PartMessage(channels = listOf("#channel", "#channel2"))))
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

