package com.jzq.netty.chat2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		out.writeBytes(
					(msg.getCmd() + "|" + msg.getSender() + "|" + msg.getReceiver() + "|" + msg.getContent() + "\n").getBytes());
	}

}
