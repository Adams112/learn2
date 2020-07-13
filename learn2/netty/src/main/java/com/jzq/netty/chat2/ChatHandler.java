package com.jzq.netty.chat2;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChatHandler extends ChannelInboundHandlerAdapter {
	private int n = 1;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(n++);
		System.out.println(Thread.currentThread());
		
		Message message = (Message) msg;
		System.out.println(message);
		if (message.getCmd().equalsIgnoreCase("login")) {
			String name = message.getSender();
			Clients.put(name, ctx);
			message.setContent("登陆成功");
			ctx.writeAndFlush(msg);
		} else {
			String receiver = message.getReceiver();
			ChannelHandlerContext to = Clients.get(receiver);
			if (to != null) {
				to.writeAndFlush(message);
				ctx.writeAndFlush(message);
			} else {
				System.out.println("接收者不存在");
			}
			
		}
	}
	
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
