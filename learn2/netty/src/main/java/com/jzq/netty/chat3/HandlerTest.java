package com.jzq.netty.chat3;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

public class HandlerTest {
	public static void main(String[] args) {
		int[] a = new int[10];
		System.out.println(Arrays.toString(a));
	}

	public static void testDecoder() {
		EmbeddedChannel channel = new EmbeddedChannel(new MessageDecoder());
		ByteBuf buf = Unpooled.buffer();
		
		Message message = new Message();
		message.setConversationType((short) 11);
		message.setMessageType1((byte) 1);
		message.setMessageType2((byte) 0);
		message.setTargetId("12345678");
		message.setUserId("87654321");
		message.setContent("this is message".getBytes());

		MessageCodec.encode(message, buf);
		channel.writeInbound(buf.retain());
		channel.finish();
		channel.readInbound();
	}
}
