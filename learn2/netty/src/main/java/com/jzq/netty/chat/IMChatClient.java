package com.jzq.netty.chat;

import java.util.Scanner;

import com.alibaba.fastjson.JSON;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class IMChatClient {
	private IMChatClientHandler clientHanlder;
	
	public static void main(String[] args) throws InterruptedException {
		new IMChatClient().start();
	}
	
	public void start() throws InterruptedException {
		Scanner in = new Scanner(System.in);
		while (true) {
			String line = in.nextLine();
			IMMessage imsg = null;
			
			try {
				imsg = JSON.parseObject(line, IMMessage.class);
			} catch(Exception e) {
				continue;
			}
			
			if (clientHanlder != null) {
				if (imsg.getCmd().equalsIgnoreCase("login")) {
					System.out.println("不要重复登陆");
				} else {
					clientHanlder.sendMsg(imsg);
				}
			} else {
				if (imsg.getCmd().equalsIgnoreCase("login")) {
					connect();
					
					Thread.sleep(5000);
					clientHanlder.sendMsg(imsg);
				} else {
					System.out.println("请先登陆");
				}
			}
			if (line.equals(""))
				in.close();
		}
	}
	
	public void connect() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				EventLoopGroup workerGroup = new NioEventLoopGroup();
				
				try {
					IMChatClientHandler localClientHanlder = new IMChatClientHandler();
					
					Bootstrap b = new Bootstrap();
					b.group(workerGroup);
					b.channel(NioSocketChannel.class);
					b.option(ChannelOption.SO_KEEPALIVE, true);
					b.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new IMDecoder());
							ch.pipeline().addLast(new IMEncoder());
							ch.pipeline().addLast(localClientHanlder);
						}
					});
					ChannelFuture f = b.connect("127.0.0.1", 8080).sync();
					clientHanlder = localClientHanlder;
					f.channel().closeFuture().sync();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					workerGroup.shutdownGracefully();
				}				
			}
		}).start();
		

	}
}
