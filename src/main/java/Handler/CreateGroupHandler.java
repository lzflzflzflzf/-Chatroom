package Handler;

import Message.CreateGroupRequest;
import Message.RcvGroupMessage;
import core.ChatServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

import java.util.ArrayList;
import java.util.Date;

public class CreateGroupHandler extends SimpleChannelInboundHandler<CreateGroupRequest> {

    SocketChannel socketChannel;


    public CreateGroupHandler(SocketChannel socketChannel)
    {
        this.socketChannel = socketChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CreateGroupRequest createGroupRequest) throws Exception {
        String from = createGroupRequest.getFrom();
        String groupName = createGroupRequest.getGroupName();

        String[] names = createGroupRequest.getNames().split(",");
        ArrayList<String> peoples = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            peoples.add(names[i]);
        }
        peoples.add(from);

        Date date = createGroupRequest.getDate();
        System.out.println(createGroupRequest);

        RcvGroupMessage rcv = new RcvGroupMessage(from,groupName,names,date);
        for (int i = 0; i < peoples.size(); i++) {
            if(ChatServer.Name2Channel.containsKey(peoples.get(i)))
            {
                System.out.println(i);
                SocketChannel socketChannel = ChatServer.Name2Channel.get(peoples.get(i));
                socketChannel.writeAndFlush(rcv);
            }
        }




    }
}
