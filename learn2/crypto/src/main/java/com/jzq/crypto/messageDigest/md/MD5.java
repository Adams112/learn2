package com.jzq.crypto.messageDigest.md;

import com.jzq.crypto.messageDigest.MessageDigestAlgorithm;

public class MD5 implements MessageDigestAlgorithm {
	private final int r[] = { 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 5, 9, 14, 20, 5, 9, 14, 20, 5,
			9, 14, 20, 5, 9, 14, 20, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 6, 10, 15, 21, 6, 10,
			15, 21, 6, 10, 15, 21, 6, 10, 15, 21 };
	private final int k[] = { 0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee, 0xf57c0faf, 0x4787c62a, 0xa8304613,
			0xfd469501, 0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be, 0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,
			0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa, 0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8, 0x21e1cde6,
			0xc33707d6, 0xf4d50d87, 0x455a14ed, 0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a, 0xfffa3942, 0x8771f681,
			0x6d9d6122, 0xfde5380c, 0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70, 0x289b7ec6, 0xeaa127fa, 0xd4ef3085,
			0x04881d05, 0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665, 0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039,
			0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1, 0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1, 0xf7537e82,
			0xbd3af235, 0x2ad7d2bb, 0xeb86d391 };

	@Override
	public byte[] digest(byte[] data) {
		int h0 = 0x67452301;
		int h1 = 0xefcdab89;
		int h2 = 0x98badcfe;
		int h3 = 0x10325476;

		// padding
		int paddingLen = data.length % 64;
		if (paddingLen >= 56)
			paddingLen = 56 + 64 - paddingLen;
		else
			paddingLen = 56 - paddingLen;

		int paddedLen = data.length + paddingLen + 8;
		byte[] paddedData = new byte[paddedLen];
		System.arraycopy(data, 0, paddedData, 0, data.length);
		System.arraycopy(getPadding(paddingLen), 0, paddedData, data.length, paddingLen);
		System.arraycopy(getByteLittleEdian((long) data.length * 8), 0, paddedData, data.length + paddingLen, 8);

		for (int i = 0; i < paddedData.length / 64; i++) {
			int a = h0, b = h1, c = h2, d = h3;
			for (int j = 0; j < 64; j++) {
				int f, g;
				if (j < 16) {
					f = (b & c) | ((~b) & d);
					g = j;
				} else if (j < 32) {
					f = (d & b) | ((~d) & c);
					g = (5 * j + 1) % 16;
				} else if (j < 48) {
					f = b ^ c ^ d;
					g = (3 * j + 5) % 16;
				} else {
					f = c ^ (b | (~d));
					g = (7 * j) % 16;
				}

				int temp = d;
				d = c;
				c = b;
				// i * 64 + (g, g + 1, g + 2, g + 3)
				int word = (paddedData[i * 64 + g * 4]) | ((paddedData[i * 64 + g * 4 + 1] & 0xff) << 8)
						| ((paddedData[i * 64 + g * 4 + 2] & 0xff) << 16) | ((paddedData[i * 64 + g * 4 + 3] & 0xff) << 24);
				b = shift(a + f + k[j] + word, r[j]) + b;
				a = temp;
			}

			h0 += a;
			h1 += b;
			h2 += c;
			h3 += d;
		}

		byte[] result = new byte[16];
		System.arraycopy(getByteLittleEdian(h0), 0, result, 0, 4);
		System.arraycopy(getByteLittleEdian(h1), 0, result, 4, 4);
		System.arraycopy(getByteLittleEdian(h2), 0, result, 8, 4);
		System.arraycopy(getByteLittleEdian(h3), 0, result, 12, 4);
		return result;
	}

	private int shift(int n, int b) {
		return (n << b) | (n >>> (32 - b));
	}

	private byte[] getByteLittleEdian(int n) {
		byte[] ret = new byte[4];
		for (int i = 0; i < 4; i++) {
			ret[i] = (byte) (n);
			n >>>= 8;
		}
		return ret;
	}

	private byte[] getPadding(int bytes) {
		byte[] ret = new byte[bytes];
		ret[0] = (byte) 0x80;
		for (int i = 1; i < bytes; i++)
			ret[i] = 0;
		return ret;
	}

	private byte[] getByteLittleEdian(long n) {
		byte[] ret = new byte[8];
		for (int i = 0; i < 8; i++) {
			ret[i] = (byte) (n);
			n >>>= 8;
		}
		return ret;
	}
}
