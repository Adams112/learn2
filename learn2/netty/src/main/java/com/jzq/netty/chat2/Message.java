package com.jzq.netty.chat2;

public class Message {
	private String cmd;
	private String sender;
	private String receiver;
	private String content;
	
	
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Message [cmd=" + cmd + ", sender=" + sender + ", receiver=" + receiver + ", content=" + content + "]";
	}
	
	
}
