package chat.willow.kale

import chat.willow.kale.irc.message.extension.batch.BatchMessage
import chat.willow.kale.irc.message.extension.sasl.rpl.Rpl903Message
import chat.willow.kale.irc.message.extension.sasl.rpl.Rpl903MessageType
import chat.willow.kale.irc.message.rfc1459.*
import chat.willow.kale.irc.prefix.Prefix
import chat.willow.kale.irc.tag.KaleTagRouter

object KaleRunner {
    @JvmStatic fun main(args: Array<String>) {
        println("Hello, Kale!")

        val kale = Kale(KaleClientRouter(), KaleMetadataFactory(KaleTagRouter()))
        val pingHandler = PingHandler()
        val pongHandler = PongHandler()
        val privMsgHandler = PrivMsgHandler()
        val rpl903Handler = Rpl903Handler()

        kale.router.register(PingMessage.command, pingHandler)
        kale.router.register(PongMessage.command, pongHandler)
        kale.router.register(PrivMsgMessage.command, privMsgHandler)
        kale.router.register(Rpl903Message.command, rpl903Handler)

        kale.process("@tag;key=value :user!host@server WHATEVER :more stuff")
        kale.process("PING :token")
        kale.process("PONG :token2")
        kale.process("NICK nickname")
        kale.process("USER username mode * realname")
        kale.process("QUIT :")
        kale.process("PASS :password")
        kale.process("JOIN #channel,#channel2 key1")
        kale.process(":nick JOIN channel account :real name")
        kale.process("PART #channel,#channel2")
        kale.process("MODE &oulu +b *!*@*.edu -e *!*@*.bu.edu")
        kale.process("PRIVMSG #mychannel :this is a message! ")
        kale.process("NOTICE #mychannel :this is a notice! ")
        kale.process(":someone INVITE user #channel")
        kale.process(":someone TOPIC #channel :a topic!")
        kale.process("KICK #channel1,#channel2 user1,user2 :kicked!")
        kale.process(":test.server 001 testnickname :welcome to test server!")
        kale.process(":test.server 002 testnickname :your host is imaginary.server, running version x")
        kale.process(":test.server 003 testnickname :this server was created date")
        kale.process(":test.server 005 testnickname KEY=VALUE KEY2= KEY3=\uD83D\uDC30")
        kale.process(":test.server 331 #channel :no topic is set")
        kale.process(":test.server 332 testnickname #channel :channel topic!")
        kale.process(":test.server 353 testnickname @ #channel :testnickname @another-nick")
        kale.process(":test.server 372 testnickname :- MOTD contents")
        kale.process(":test.server 375 testnickname :- test.server Message of the day - ")
        kale.process(":test.server 376 testnickname :End of MOTD command")
        kale.process(":test.server 422 testnickname :MOTD File is missing")
        kale.process("BATCH +reference type param1")
        kale.process("BATCH -reference")
        kale.process(":test.server 903 testnickname :message")

        kale.process("@account=testuser;time=2012-06-30T23:59:60.419Z PRIVMSG #mychannel :this is a message! ")

        println(kale.serialise(PingMessage.Command(token = "token")))
        println(kale.serialise(PongMessage.Message(token = "token2")))
        println(kale.serialise(NickMessage.Command(nickname = "nickname")))
        println(kale.serialise(UserMessage.Command(username = "username", mode = "mode", realname = "realname")))
        println(kale.serialise(QuitMessage.Command(message = "")))
        println(kale.serialise(PassMessage.Command(password = "password")))
        println(kale.serialise(JoinMessage.Command(channels = listOf("#channel", "#channel2"), keys = listOf("key1"))))
        println(kale.serialise(PartMessage.Command(channels = listOf("#channel", "#channel2"))))
        println(kale.serialise(ModeMessage.Command(target = "#channel", modifiers = listOf(ModeMessage.ModeModifier(type = '+', mode = 'b', parameter = "somebody")))))
        println(kale.serialise(PrivMsgMessage.Command(target = "person", message = "hello")))
        println(kale.serialise(PrivMsgMessage.Command(target = "#channel", message = "I am a bot")))
        println(kale.serialise(InviteMessage.Message(source = Prefix(nick = "someone"), user = "user", channel = "#channel")))
        println(kale.serialise(TopicMessage.Message(source = Prefix(nick = "someone"), channel = "#channel", topic = "a topic!")))
        println(kale.serialise(KickMessage.Command(channels = listOf("#channel1", "#channel2"), users = listOf("user1", "user2"), comment = "kicked!")))
        println(kale.serialise(BatchMessage.Start.Message(source = Prefix(nick = "someone"), reference = "reference", type = "type", parameters = listOf("parameter1", "parameter2"))))
        println(kale.serialise(BatchMessage.End.Message(source = Prefix(nick = "someone"), reference = "reference")))
    }

    class PingHandler: KaleHandler<PingMessage.Command>(PingMessage.Command.Parser) {

        override fun handle(message: PingMessage.Command, metadata: IMetadataStore) {
            println("handling ping: $message")
        }

    }

    class PongHandler: KaleHandler<PongMessage.Message>(PongMessage.Message.Parser) {

        override fun handle(message: PongMessage.Message, metadata: IMetadataStore) {
            println("handling pong: $message")
        }

    }

    class PrivMsgHandler: KaleHandler<PrivMsgMessage.Message>(PrivMsgMessage.Message.Parser) {

        override fun handle(message: PrivMsgMessage.Message, metadata: IMetadataStore) {
            println("handling message: $message")
        }

    }

    class Rpl903Handler: KaleHandler<Rpl903MessageType>(Rpl903Message.Parser) {

        override fun handle(message: Rpl903MessageType, metadata: IMetadataStore) {
            println("handling rpl 903: $message")
        }


    }
}

