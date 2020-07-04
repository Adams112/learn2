package com.jzq.netty.print;

import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class PrintServer {
	public void start(int port) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 128)
				.childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
						ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF8")));
						ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF8")));
						ch.pipeline().addLast(new PrintHandler());
					}
				});
            ChannelFuture f = bootstrap.bind(port).sync();
            System.out.println("server start done");
            f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		
	}
	
	public static void main(String[] args) {
		new PrintServer().start(8080);
	}
}
