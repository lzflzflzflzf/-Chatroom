package Handler;

import Message.LoginRequestMessage;
import Message.LoginResponseMessage;
import UserService.UserService;
import core.ChatServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class LoginHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    SocketChannel socketChannel;

    public LoginHandler(SocketChannel socketChannel)
    {
        this.socketChannel = socketChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestMessage loginRequestMessage) throws Exception {
        String name = loginRequestMessage.getUserName();
        String password = loginRequestMessage.getPassWord();
        boolean LoginSuccess = UserService.loginSuccess(name, password);

        LoginResponseMessage message;
        if(LoginSuccess==true)
        {
            message = new LoginResponseMessage(true,"123123123123");
            ChatServer.Name2Channel.put(name,socketChannel);
        }
        else
        {
            message = new LoginResponseMessage(false,"用户名或密码错误");
        }
        socketChannel.writeAndFlush(message);
    }


}

