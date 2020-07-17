package com.jzq.netty.chat3;

import java.util.Arrays;

public class Message {
	private short conversationType;
	private byte messageType1;
	private byte messageType2;
	private String targetId;
	private String userId;
	private byte[] content;

	public short getConversationType() {
		return conversationType;
	}

	public void setConversationType(int conversationType) {
		this.conversationType = (short) conversationType;
	}

	public byte getMessageType1() {
		return messageType1;
	}

	public void setMessageType1(int messageType1) {
		this.messageType1 = (byte) messageType1;
	}

	public byte getMessageType2() {
		return messageType2;
	}

	public void setMessageType2(int messageType2) {
		this.messageType2 = (byte) messageType2;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Message [conversationType=" + conversationType + ", messageType1="
				+ messageType1 + ", messageType2=" + messageType2 + ", targetId=" + targetId + ", userId=" + userId
				+ ", content=" + Arrays.toString(content) + "]";
	}

}
