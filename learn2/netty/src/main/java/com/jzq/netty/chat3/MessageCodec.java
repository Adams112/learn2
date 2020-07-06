package com.jzq.netty.chat3;

import io.netty.buffer.ByteBuf;

public class MessageCodec {
	public static short MAGIC_NUMBER = 0x7890;
	
	public static Message decode(ByteBuf in) {
		int readableBytes = in.readableBytes();
		
		if (readableBytes < 4)
			return null;
		
		in.markReaderIndex();
		short magicNumber = in.readShort();
		if (magicNumber != MAGIC_NUMBER) return null;
		
		short packetLen = in.readShort();
		if (packetLen < readableBytes) {
			in.resetReaderIndex();
			return null;
		}
		
		Message message = new Message();
		message.setConversationType(in.readShort());
		message.setMessageType1(in.readByte());
		message.setMessageType2(in.readByte());
		byte[] targetId = new byte[24];
		in.readBytes(targetId);
		byte[] userId = new byte[24];
		in.readBytes(userId);
		
		int targetLen = 0, userLen = 0;
		while (targetId[targetLen] != 0) targetLen++;
		while (userId[userLen] != 0) userLen++;
		
		if (targetLen == 0)
			message.setTargetId(null);
		else 
			message.setTargetId(new String(targetId, 0, targetLen));
		
		if (userLen == 0)
			message.setUserId(null);
		else 
			message.setUserId(new String(userId, 0, userLen));
		
		int contentLen = packetLen - 56;
		if (contentLen == 0) 
			message.setContent(null);
		else {
			byte[] content = new byte[contentLen];
			in.readBytes(content);
			message.setContent(content);
		}
		
		return message;
	}
	
	public static void encode(Message message, ByteBuf buf) {
		buf.writeShort(MAGIC_NUMBER);
		
		int contentLen = (message.getContent() == null) ?
							0 :
							message.getContent().length;
		int totalLen = contentLen + 56;
		buf.writeShort(totalLen);
		
		buf.writeShort(message.getConversationType());
		buf.writeByte(message.getMessageType1());
		buf.writeByte(message.getMessageType2());
		
		byte[] targetIdToWrite = new byte[24];
		if (message.getTargetId() != null) {
			byte[] targetId = message.getTargetId().getBytes();
			System.arraycopy(targetId, 0, targetIdToWrite, 0, targetId.length);
		}
		
		

		byte[] userIdToWrite = new byte[24];
		if (message.getUserId() != null) {
			byte[] userId = message.getUserId().getBytes();
			System.arraycopy(userId, 0, userIdToWrite, 0, userId.length);
		}
	
		buf.writeBytes(targetIdToWrite);
		buf.writeBytes(userIdToWrite);
		if (message.getContent() != null)
			buf.writeBytes(message.getContent());
	}

}
