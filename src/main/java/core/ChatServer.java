package core;

import Handler.ChatHandler;
import Handler.CreateGroupHandler;
import Handler.LoginHandler;
import Handler.RegisterHandler;
import Message.LoginRequestMessage;
import Message.LoginResponseMessage;
import Message.RegisterRequestMessage;
import Message.RegisterResponseMessage;
import UserService.UserService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.LoggerFactory;
import protocol.MessageCodec;
import protocol.ProtocolFrameDecoder;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    static final org.slf4j.Logger log = LoggerFactory.getLogger(ChatServer.class);
    public static ConcurrentHashMap<String,SocketChannel> Name2Channel = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(4);

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss,worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ProtocolFrameDecoder());
                    socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    socketChannel.pipeline().addLast(new MessageCodec());
                    socketChannel.pipeline().addLast(new LoginHandler(socketChannel));
                    socketChannel.pipeline().addLast(new RegisterHandler(socketChannel));
                    socketChannel.pipeline().addLast(new ChatHandler(socketChannel));
                    socketChannel.pipeline().addLast(new CreateGroupHandler(socketChannel));


                }
            });
            serverBootstrap.bind(8088);

        } finally {

        }


    }
}
