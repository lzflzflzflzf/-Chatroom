package Handler;

import Message.RegisterRequestMessage;
import Message.RegisterResponseMessage;
import UserService.UserService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class RegisterHandler extends SimpleChannelInboundHandler<RegisterRequestMessage> {

    SocketChannel socketChannel;

    public RegisterHandler(SocketChannel socketChannel)
    {
        this.socketChannel = socketChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RegisterRequestMessage registerRequestMessage) throws Exception {
        String name = registerRequestMessage.getName();
        String password = registerRequestMessage.getPassword();

        Boolean successRegister = UserService.Register(name,password);

        RegisterResponseMessage message;
        if(successRegister){
            message = new RegisterResponseMessage(true,"123");
        }
        else
        {
            message = new RegisterResponseMessage(false,"用户名不能重复");
        }
        socketChannel.writeAndFlush(message);

    }

}
