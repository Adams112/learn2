package com.jzq.util.byteUtil;

public class ByteOp {

	/*
	 * 大小端模式 小端模式： 数据高位保存在内存高地址 大端模式：数据高位保存在内存低地址 提供基本数据类型到byte的互转
	 */

	public static byte[] getBytesLE(byte n) {
		return new byte[] { n };
	}

	public static byte[] getBytesLE(short n) {
		return new byte[] { (byte) (n & 0xff), (byte) ((n >> 8) & 0xff) };
	}

	public static byte[] getBytesLE(int n) {
		byte[] res = new byte[4];
		for (int i = 0; i < 4; i++) {
			res[i] = (byte) (n & 0xff);
			n >>>= 8;
		}
		return res;
	}

	public static byte[] getBytesLE(long n) {
		byte[] res = new byte[8];
		for (int i = 0; i < 8; i++) {
			res[i] = (byte) (n & 0xff);
			n >>>= 8;
		}
		return res;
	}

	public static byte[] getBytesLE(float n) {
		return getBytesLE(Float.floatToIntBits(n));
	}

	public static byte[] getBytesLE(double n) {
		return getBytesLE(Double.doubleToLongBits(n));
	}

	public static byte getByteLE(byte[] bytes) {
		if (bytes == null || bytes.length < 1)
			throw new ArrayIndexOutOfBoundsException(2);

		return bytes[0];
	}

	public static short getShortLE(byte[] bytes) {
		if (bytes == null || bytes.length < 2)
			throw new ArrayIndexOutOfBoundsException(2);

		return (short) (bytes[0] | (bytes[1] << 8));
	}

	public static short getShortLE(byte[] bytes, int index) {
		if (bytes == null || bytes.length - index < 2)
			throw new ArrayIndexOutOfBoundsException(index + 2);

		return (short) (bytes[index] | (bytes[index + 1] << 8));
	}

	public static int getIntLE(byte[] bytes) {
		if (bytes == null || bytes.length < 4)
			throw new ArrayIndexOutOfBoundsException(4);

		return (bytes[0] | (bytes[1] << 8) | (bytes[2] << 16) | (bytes[3] << 24));
	}

	public static int getIntLE(byte[] bytes, int index) {
		if (bytes == null || bytes.length - index < 4)
			throw new ArrayIndexOutOfBoundsException(index + 4);

		return (bytes[index + 0] | (bytes[index + 1] << 8) | (bytes[index + 2] << 16) | (bytes[index + 3] << 24));
	}

	public static long getLongLE(byte[] bytes) {
		if (bytes == null || bytes.length < 8)
			throw new ArrayIndexOutOfBoundsException(8);

		long n = bytes[0];
		for (int i = 1; i < 8; i++) {
			n |= (bytes[i] << (8 * i));
		}
		return n;
	}

	public static long getLongLE(byte[] bytes, int index) {
		if (bytes == null || bytes.length - index < 8)
			throw new ArrayIndexOutOfBoundsException(index + 8);

		long n = bytes[index];
		for (int i = 1; i < 8; i++) {
			n |= (bytes[index + i] << (8 * i));
		}
		return n;
	}

	public static float getFloatLE(byte[] bytes) {
		return Float.intBitsToFloat(getIntLE(bytes));
	}

	public static float getFloatLE(byte[] bytes, int index) {
		return Float.intBitsToFloat(getIntLE(bytes, index));
	}

	public static double getDoubleLE(byte[] bytes) {
		return Double.longBitsToDouble(getLongLE(bytes));
	}

	public static double getDoubleLE(byte[] bytes, int index) {
		return Double.longBitsToDouble(getLongLE(bytes, index));
	}

	// Big Endian
	public static byte[] getBytesBE(short n) {
		return new byte[] { (byte) ((n >> 8) & 0xff), (byte) (n & 0xff) };
	}

	public static byte[] getBytesBE(int n) {
		byte[] res = new byte[4];
		for (int i = 0; i < 4; i++) {
			res[4 - 1 - i] = (byte) (n & 0xff);
			n >>>= 8;
		}
		return res;
	}

	public static byte[] getBytesBE(long n) {
		byte[] res = new byte[8];
		for (int i = 0; i < 8; i++) {
			res[8 - 1 - i] = (byte) (n & 0xff);
			n >>>= 8;
		}
		return res;
	}

	public static byte[] getBytesBE(float n) {
		return getBytesBE(Float.floatToIntBits(n));
	}

	public static byte[] getBytesBE(double n) {
		return getBytesBE(Double.doubleToLongBits(n));
	}

	public static short getShortBE(byte[] bytes) {
		if (bytes == null || bytes.length < 2)
			throw new ArrayIndexOutOfBoundsException(2);

		return (short) ((bytes[0] << 8) | bytes[1]);
	}

	public static short getShortBE(byte[] bytes, int index) {
		if (bytes == null || bytes.length - index < 2)
			throw new ArrayIndexOutOfBoundsException(index + 2);

		return (short) ((bytes[index] << 8) | bytes[index + 1]);
	}

	public static int getIntBE(byte[] bytes) {
		if (bytes == null || bytes.length < 4)
			throw new ArrayIndexOutOfBoundsException(4);

		return ((bytes[0] << 24) | (bytes[1] << 16) | (bytes[2] << 8) | (bytes[3]));
	}

	public static int getIntBE(byte[] bytes, int index) {
		if (bytes == null || bytes.length - index < 4)
			throw new ArrayIndexOutOfBoundsException(index + 4);

		return ((bytes[index + 0] << 24) | (bytes[index + 1] << 16) | (bytes[index + 2] << 8) | (bytes[index + 3]));
	}

	public static long getLongBE(byte[] bytes) {
		if (bytes == null || bytes.length < 8)
			throw new ArrayIndexOutOfBoundsException(8);

		long n = bytes[0];
		for (int i = 1; i < 8; i++) {
			n <<= 8;
			n |= bytes[i];
		}
		return n;
	}

	public static long getLongBE(byte[] bytes, int index) {
		if (bytes == null || bytes.length - index < 8)
			throw new ArrayIndexOutOfBoundsException(index + 8);

		long n = bytes[index];
		for (int i = 1; i < 8; i++) {
			n <<= 8;
			n |= bytes[index + i];
		}
		return n;
	}

	public static float getFloatBE(byte[] bytes) {
		return Float.intBitsToFloat(getIntBE(bytes));
	}

	public static float getFloatBE(byte[] bytes, int index) {
		return Float.intBitsToFloat(getIntBE(bytes, index));
	}

	public static double getDoubleBE(byte[] bytes) {
		return Double.longBitsToDouble(getLongBE(bytes));
	}

	public static double getDoubleBE(byte[] bytes, int index) {
		return Double.longBitsToDouble(getLongBE(bytes, index));
	}

	/*
	 * <<, >>, >>> >>移位运算对于不足int的类型的负数会补足高位为1 >>>补0
	 */

	// 循环移位
	public static byte leftRoundShift(byte n, int bits) {
		return (byte) ((n << bits) | n >>> (8 - bits));
	}

	public static short leftRoundShift(short n, int bits) {
		return (byte) ((n << bits) | n >>> (16 - bits));
	}

	public static int leftRoundShift(int n, int bits) {
		return (byte) ((n << bits) | n >>> (32 - bits));
	}

	public static long leftRoundShift(long n, int bits) {
		return (byte) ((n << bits) | n >>> (64 - bits));
	}

	public static byte rightRoundShift(byte n, int bits) {
		return (byte) ((n << (8 - bits)) | n >>> (bits));
	}

	public static short rightRoundShift(short n, int bits) {
		return (byte) ( (n << (16 - bits)) | n >>> (bits));
	}

	public static int rightRoundShift(int n, int bits) {
		return (byte) ( (n << (32 - bits)) | n >>> (bits));
	}

	public static long rightRoundShift(long n, int bits) {
		return (byte) ( (n << (64 - bits)) | n >>> (bits));
	}

	// byte to hex
	private static final char[] arr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static final char[] arr2 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	private static final String prefix = "0x";
	
	public static String byteToHex(byte n) {
		return prefix + arr[(n >> 4) & 0xf] + arr[n & 0xf];
	}
	
	public static String byteToHexUpper(byte n) {
		return prefix + arr2[(n >> 4) & 0xf] + arr2[n & 0xf];
	}
	
	public static String byteToHex(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(prefix);
		for (byte b : bytes) {
			buffer.append(arr[(b >> 4) & 0xf]);
			buffer.append(arr[b & 0xf]);
		}
		return buffer.toString();
	}
	
	public static String byteToHexUpper(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(prefix);
		for (byte b : bytes) {
			buffer.append(arr2[(b >> 4) & 0xf]);
			buffer.append(arr2[b & 0xf]);
		}
		return buffer.toString();
	}
	
	public static byte[] hexToByte(String hex) {
		if (hex == null || hex.length() < 3 || !hex.startsWith(prefix))
			throw new IllegalArgumentException(hex + " is not hex number");
		
		int hexLen = hex.length();
		int byteLen = (hexLen - 2) / 2;
		if (hexLen % 2 == 1)
			byteLen += 1;
		
		byte[] res = new byte[byteLen];
		int b = 0;
		boolean decoded = hexLen % 2 == 1;
		int byteIndex = 0;
		for (int i = 2; i < hexLen; i++) {
			char ch = hex.charAt(i);
			int num = hexToByteHelper(ch);
			if (decoded) {
				b = (b << 4) + num;
				res[byteIndex++] = (byte) b;
				b = 0;
				decoded = false;
			} else {
				b = num;
			}
		}
		return res;
	}
	
	private static int hexToByteHelper(char ch) {
		if ('0' <= ch && ch <= '9')
			return ch - '0';
		if ('a' <= ch && ch <= 'f')
			return ch - 'a' + 10;
		if ('A' <= ch && ch <= 'F')
			return ch - 'A' + 10;
		
		throw new IllegalArgumentException(ch + "is not hex charactor");
	}
}
