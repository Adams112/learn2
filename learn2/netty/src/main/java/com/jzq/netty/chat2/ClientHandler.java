package com.jzq.netty.chat2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
	String clientName;
	
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
    	Message message = (Message) obj;
    	
    	if (message.getCmd().equalsIgnoreCase("login")) {
    		System.out.println("system: " + message.getContent());
    		clientName = message.getSender();
    	} else {
    		if (message.getSender().equals(clientName)) {
    			System.out.println("you: " + message.getContent());
    		} else {
    			System.out.println(message.getSender() + ": " + message.getContent());
    		}
    	}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
