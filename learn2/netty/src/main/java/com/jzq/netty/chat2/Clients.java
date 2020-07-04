package com.jzq.netty.chat2;

import java.util.HashMap;

import io.netty.channel.ChannelHandlerContext;

public class Clients {
	private static HashMap<String, ChannelHandlerContext> clients = new HashMap<String, ChannelHandlerContext>();
	
	static void put(String name, ChannelHandlerContext client) {
		clients.put(name, client);
	}
	
	static ChannelHandlerContext get(String name) {
		return clients.get(name);
	}
}
