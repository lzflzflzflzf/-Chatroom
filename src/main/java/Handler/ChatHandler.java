package Handler;

import Message.ChatMessage;
import Message.ChatRequestMessage;
import Message.ChatResponseMessage;
import Message.LoginRequestMessage;
import core.ChatServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

import java.util.Date;


public class ChatHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    private SocketChannel socketChannel;

    public ChatHandler(SocketChannel socketChannel)
    {
        this.socketChannel = socketChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatRequestMessage chatRequestMessage) throws Exception {
        String from = chatRequestMessage.getFrom();
        String to = chatRequestMessage.getTo();
        String content = chatRequestMessage.getContent();
        Date messageDate = chatRequestMessage.getDate();

        if(!ChatServer.Name2Channel.containsKey(to))
        {
            //当对方不在线时，返回发送失败
            //此处之后可以拓展，当对方不在线时，先将消息存储起来
            ChatResponseMessage message = new ChatResponseMessage(false,"对方不在线");
            socketChannel.writeAndFlush(message);
            return;
        }

        SocketChannel ToChannel = ChatServer.Name2Channel.get(to);
        ChatMessage chatMessage = new ChatMessage(from,content,messageDate);
        ToChannel.writeAndFlush(chatMessage);


    }
}
