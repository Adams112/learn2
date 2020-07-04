package com.jzq.netty.chat2;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int len = in.readableBytes();
		final byte[] array = new byte[len];
		in.getBytes(in.readerIndex(), array, 0, len);
		String content = new String(array, "GBK");
		String[] split = content.split("\\|");
		
		Message message = new Message();
		message.setCmd(split[0]);
		message.setSender(split[1]);
		message.setReceiver(split.length >= 3 ? split[2] : null);
		message.setContent(split.length >= 4 ? split[3] : null);
		
		out.add(message);
		in.clear();
	}

}
