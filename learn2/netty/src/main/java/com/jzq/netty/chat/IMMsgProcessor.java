package com.jzq.netty.chat;


import com.alibaba.fastjson.JSON;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class IMMsgProcessor {
	private static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	
	public void processMsg(Channel client, String msg, IMMessage imsg) {
		System.out.println(msg);
		if (imsg.getCmd().equalsIgnoreCase("LOGIN")) {
			onlineUsers.add(client);
			
			for (Channel channel : onlineUsers) {
				IMMessage send = new IMMessage();
				if (client == channel) {
					send.setSender("SYSTEM");
					send.setTime(System.currentTimeMillis());
					send.setContent("已与服务器建立连接");
				} else {
					send = new IMMessage();
					send.setSender("SYSTEM");
					send.setTime(System.currentTimeMillis());
					send.setContent(imsg.getSender() + "加入聊天");
				}
				channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(send)));
			}
			

		} else if (imsg.getCmd().equalsIgnoreCase("CHAT")) {
			for (Channel channel : onlineUsers) {
				IMMessage send = new IMMessage();
				if (client == channel) {
					send.setSender("you");
				} else {
					send.setSender(imsg.getSender());
				}
				send.setTime(System.currentTimeMillis());
				send.setContent(imsg.getContent());
				channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(send)));
			}
		} else if (imsg.getCmd().equalsIgnoreCase("LOGOUT")) {
			if (onlineUsers.remove(client)) {
				for (Channel channel : onlineUsers) {
					IMMessage send = new IMMessage();
					if (channel == client) {
						send.setSender("SYSTEM");
						send.setTime(System.currentTimeMillis());
						send.setContent("你已退出群聊");
					} else {
						send.setSender("SYSTEM");
						send.setTime(System.currentTimeMillis());
						send.setContent(imsg.getSender() + "已退出群聊");
					}
				}
			}
		}
	}
	
}
