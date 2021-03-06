package core;

import Message.*;
import UserService.Group;
import UserService.GroupOperator;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.LoggerFactory;
import protocol.MessageCodec;
import protocol.ProtocolFrameDecoder;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatClient {


    static final org.slf4j.Logger log = LoggerFactory.getLogger(ChatServer.class);



    public static void main(String[] args) {
        GroupOperator groupOperator = new GroupOperator();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        Scanner in = new Scanner(System.in);
        CountDownLatch waitLogin = new CountDownLatch(1);
        CountDownLatch waitRegister = new CountDownLatch(1);
        AtomicBoolean LOGIN = new AtomicBoolean(false);
        AtomicBoolean REGISTER = new AtomicBoolean(false);



        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ProtocolFrameDecoder());
//                    socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    socketChannel.pipeline().addLast(new MessageCodec());


                    socketChannel.pipeline().addLast("client handler",new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            if(msg instanceof LoginResponseMessage)
                            {
                                waitLogin.countDown();
                                if(((LoginResponseMessage) msg).isSuccess())
                                {
                                    LOGIN.set(true);
                                }
                            }
                            else if(msg instanceof RegisterResponseMessage){
                                waitRegister.countDown();
                                if(((RegisterResponseMessage) msg).isSuccess()){
                                    REGISTER.set(true);
                                }
                            }
                            else if(msg instanceof ChatMessage){
                                ChatMessage message = ((ChatMessage) msg);
                                SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
                                String str = sdf.format(message.getDate());
                                System.out.println(str+"  "+message.getFrom()+" ??????????????????: "+message.getContent());
                            }
                            else if(msg instanceof ChatResponseMessage){
                                System.out.println(((ChatResponseMessage) msg).getReason());
                            }
                            else if(msg instanceof RcvGroupMessage){
                                String from = ((RcvGroupMessage) msg).getFrom();
                                String groupName = ((RcvGroupMessage) msg).getGroupName();
                                String[] names = ((RcvGroupMessage) msg).getNames();
                                Date date = ((RcvGroupMessage) msg).getDate();
                                SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
                                String str = sdf.format(date);
                                System.out.print(str+"  "+from+"?????????????????????:"+groupName+" ?????????????????????");
                                for (int i = 0; i < names.length; i++) {
                                    System.out.print(names[i]+"  ");
                                }
                                System.out.println();
                                groupOperator.add(new Group(groupName,date,from));
                            }
                        }

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {

                            //?????????????????????????????????????????????????????????????????????
                            //?????????????????????????????????????????????nio??????????????????????????????????????????????????????
                            // ????????????????????????????????????LoginRequestMessage???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????nio??????????????????????????????????????????
                            new Thread(()->{
                                String userName = null;
FIRST:                      while(true)
                                {
                                    System.out.println("?????????????????????????????????????????????1??????????????????2");
                                    String choose = in.nextLine();
                                    if(choose.equals("1"))
                                    {
                                        //?????????????????????
                                        userName = in.nextLine();
                                        LoginRequestMessage l = new LoginRequestMessage(userName,"li");
                                        ctx.writeAndFlush(l);
                                        System.out.println("???????????????????????????");
                                        try {
                                            waitLogin.await();
                                        }catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        System.out.println("????????????");
                                        if(LOGIN.get()){
                                            System.out.println("????????????");
                                            break FIRST;
                                        }
                                        else
                                        {
                                            //????????????
                                            System.out.println("????????????");
                                        }
                                    }
                                    else if(choose.equals("2"))
                                    {
                                        //????????????
                                        RegisterRequestMessage requestMessage = new RegisterRequestMessage("newuser","newpsw");
                                        ctx.writeAndFlush(requestMessage);
                                        System.out.println("???????????????????????????");
                                        try {
                                            waitRegister.await();
                                        }catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        System.out.println("????????????");
                                        if(REGISTER.get()){
                                            System.out.println("????????????");
                                            break FIRST;
                                        }
                                        else
                                        {
                                            //????????????

                                        }
                                    }
                                }



                                while(true) {
                                    System.out.println("==================================");
                                    System.out.println("send [username] [content]");
                                    System.out.println("gsend [group name] [content]");
                                    System.out.println("gcreate [group name] [m1,m2,m3...]");
                                    System.out.println("gmembers [group name]");
                                    System.out.println("gjoin [group name]");
                                    System.out.println("gquit [group name]");
                                    System.out.println("list friend");
                                    System.out.println("list group");
                                    System.out.println("quit");
                                    System.out.println("==================================");
                                    String command = in.nextLine();
                                    // ??????????????????????????????????????????????????????
                                    String[] commands = command.split(" ");
                                    switch (commands[0]) {
                                        case "send":
                                            ctx.writeAndFlush(new ChatRequestMessage(userName,commands[1],commands[2],new Date()));
                                            break;
                                        case "gsend":

                                            break;
                                        case "gcreate":
                                            ctx.writeAndFlush(new CreateGroupRequest(userName,commands[1],commands[2],new Date()));
                                            break;
                                        case "gmembers":

                                            break;
                                        case "gjoin":

                                            break;
                                        case "gquit":

                                            break;
                                        case "quit":
                                            ctx.channel().close();
                                            return;
                                        default:
                                            System.out.println("??????????????????????????????");
                                            continue;

                                    }
                                }




                            }).start();
                        }
                    });
                }
            });

            Channel channel = bootstrap.connect("localhost", 8088).sync().channel();


            channel.closeFuture().sync();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
        }



    }

}
