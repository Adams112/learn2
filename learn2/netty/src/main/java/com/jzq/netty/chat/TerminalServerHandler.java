package com.jzq.netty.chat;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TerminalServerHandler extends SimpleChannelInboundHandler<IMMessage> {

	private IMMsgProcessor processor = new IMMsgProcessor();
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IMMessage imsg) throws Exception {
		System.out.println(imsg);
		processor.processMsg(ctx.channel(), JSON.toJSONString(imsg), imsg);
	}

}
