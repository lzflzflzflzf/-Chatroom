package protocol;

import Message.Message;
import TestUtils.ByteBufferUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message m, ByteBuf buf) throws Exception {
        //设置魔数  4字节
        buf.writeBytes(new byte[]{'0','6','2','2'});
        //设置版本号  1字节
        buf.writeByte('1');

        //设置指令类型  4字节
        buf.writeInt(m.getType());

        //补齐指令  16-4-1-4-4=3
        buf.writeBytes(new byte[]{'1','1','1'});

        //获得序列后的msg
        ByteArrayOutputStream bos = new ByteArrayOutputStream();//通过ByteArrayOutputStream拿到最终结果
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {//ObjectOutputStream 作用是把对象转换成二进制的字节数组
            oos.writeObject(m);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        byte[] bytes = bos.toByteArray();//Java对象变成字节数组

        //写入长度  4字节
        buf.writeInt(bytes.length);
        // 写入内容
        buf.writeBytes(bytes);
        //调试
//        ByteBufferUtil.log(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // 获取魔数       4字节
        int magic = in.readInt();
        // 获取版本号     1字节
        byte version = in.readByte();
        // 获得指令类型   4字节
        int messageType = in.readInt();
        // 移除补齐字节   3字节
        in.readByte(); in.readByte(); in.readByte();
        // 获得正文长度   4字节
        int length = in.readInt();
        // 获得正文
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message message = (Message) ois.readObject();
        // 将信息放入List中，传递给下一个handler
        out.add(message);

    }
}
