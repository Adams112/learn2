package com.jzq.netty.chat3;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageStoreHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("消息已经保存:" +  msg);
		
		super.channelRead(ctx, msg);
	}
	
}
