package com.jzq.netty.chat;

import java.util.List;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class IMDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			int len = in.readableBytes();
			System.out.println("len:" + len);
			final byte[] array = new byte[len];
			String content = new String(array, in.readerIndex(), len);
			
			if (null == content || "".equals(content.trim())) {
				ctx.channel().pipeline().remove(this);
				return;
			}
			
			in.getBytes(in.readerIndex(), array, 0, len);
			out.add(JSON.parseObject(content, IMMessage.class));
			in.clear();
		} catch (Exception e) {
			ctx.channel().pipeline().remove(this);
		}
	}
		
}
