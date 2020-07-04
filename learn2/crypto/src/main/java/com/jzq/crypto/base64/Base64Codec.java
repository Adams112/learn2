package com.jzq.crypto.base64;

public enum Base64Codec {

	base64CodecBouncy(43, 92, 61, false), base64CodecCommon(43, 92, 61, true), urlBase64CodecBouncy(45, 95, 46, false),
	urlBase64CodecCommon(45, 95, 0, false);

	public String decode(String str) {
		byte[] toDecode = str.getBytes();
		byte[] decoded = new byte[toDecode.length];
		int compose = 0;
		int composedBit = 0;
		int curIndex = 0;
		
		for (byte b : toDecode) {
			byte afterDecode = decode(b);
			if (afterDecode < 64) {
				compose = (compose << 6) + afterDecode;
				composedBit += 6;
				
				if (composedBit >= 8) {
					decoded[curIndex++] = (byte) (compose >> (composedBit -= 8));
					compose &= ((0x1 << composedBit) - 1);
				}
			}
		}
		
		return new String(decoded, 0, curIndex);
	}
	
	private byte decode(int b) {
		if ('A' <= b && b <= 'Z') 
			return (byte) (b - 'A');
		else if ('a' <= b && b <= 'z')
			return (byte) (b - 'a' + 26);
		else if ('0' <= b && b <= '9')
			return (byte) (b - '0' + 52);
		else if (b == b62)
			return 62;
		else if (b == b63)
			return 63;
		else // if (b == pad || b == '\r' || b == '\n')
			return 64;
	}
	

	public String encode(String str) {
		byte[] toEncode = str.getBytes();
		int toEncodeLen = toEncode.length;
		
		int encodedLen = (toEncodeLen / 3) * 4;
		int paddingCount = 0;
		if (pad != 0) {
			if (toEncodeLen % 3 != 0) {
				encodedLen += 4;
				if (toEncodeLen % 3 == 1)
					paddingCount = 2;
				else
					paddingCount = 1;
			}
		} else {
			if (toEncodeLen % 3 == 1)
				encodedLen += 2;
			else if (toEncodeLen % 3 == 2)
				encodedLen += 3;
		}
		
		int temp = encodedLen;
		if (rn) {
			encodedLen += (temp / 76) * 2;
			if (temp % 76 != 0)
				encodedLen += 2;
		}
		
		
		byte[] encoded = new byte[encodedLen];
		
		int curIndex = 0, base64Count = 0;
		int remain = 0;
		int remainBit = 0;
		for (byte b : toEncode) {
			remain = (remain << 8) | (0x000000ff & b);
			remainBit += 8;
			
			while (remainBit >= 6) {
				byte afterEncode = encode(remain >> (remainBit -= 6));
				encoded[curIndex++] = afterEncode;
				remain = remain & ((0x1 << (remainBit)) - 1);
				base64Count++;
				
				if (rn && base64Count % 76 == 0) {
					encoded[curIndex++] = '\r';
					encoded[curIndex++] = '\n';
				}
			}
		}
		
		if (remain != 0) {
			encoded[curIndex++] = encode(remain <<= (6 - remainBit));
			if (rn && base64Count % 76 == 0) {
				encoded[curIndex++] = '\r';
				encoded[curIndex++] = '\n';
			}
		}
		
		
		while (paddingCount-- > 0)
			encoded[curIndex++] = pad;
		
		if (rn) {
			encoded[curIndex++] = '\r';
			encoded[curIndex++] = '\n';
		}
		
		return new String(encoded);
	}
	
	private byte encode(int b) {
		if (b < 26)
			return (byte) (b + 'A');
		else if (b < 52)
			return (byte) (b - 26 + 'a');
		else if (b < 62)
			return (byte) (b - 52 + '0');
		else if (b == 62)
			return b62;
		else 
			return b63;
	}
	
	private byte b62;
	private byte b63;
	private byte pad;
	private boolean rn;
	
	private Base64Codec(int b62, int b63, int pad, boolean rn) {
		this.b62 = (byte) b62;
		this.b63 = (byte) b63;
		this.pad = (byte) pad;
		this.rn = rn;
	}

}
