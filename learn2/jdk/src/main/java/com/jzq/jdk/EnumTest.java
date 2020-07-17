package com.jzq.jdk;

public class EnumTest {
	static enum E {
		A, B, C;
	}
	
	public static void main(String[] args) {
		// "A"
		System.out.println(E.A.name());
		
		// 1
		System.out.println(E.B.ordinal());
		
	}
}
