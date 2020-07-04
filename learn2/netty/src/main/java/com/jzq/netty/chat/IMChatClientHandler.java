package com.jzq.netty.chat;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class IMChatClientHandler extends SimpleChannelInboundHandler<IMMessage> {
	private ChannelHandlerContext ctx;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IMMessage imsg) throws Exception {
		System.out.println(imsg);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	public void sendMsg(IMMessage msg) {
		System.out.println(msg);
		ctx.channel().writeAndFlush(JSON.toJSON(msg));
	}
}
