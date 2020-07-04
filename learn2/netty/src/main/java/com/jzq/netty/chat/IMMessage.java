package com.jzq.netty.chat;

public class IMMessage {
	private String addr;
	private String cmd;
	private long time;
	private int online;
	private String sender;
	private String receiver;
	private String content;
	private String terminal;

	public IMMessage() {
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
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

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	@Override
	public String toString() {
		return "IMMessage [addr=" + addr + ", cmd=" + cmd + ", time=" + time + ", online=" + online + ", sender="
				+ sender + ", receiver=" + receiver + ", content=" + content + ", terminal=" + terminal + "]";
	}
	
	
}
