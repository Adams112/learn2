package com.jzq.jdk;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {
	public static void main(String[] args) {
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
		map.put("1", "1");
		map.put("2", "1");
		map.put("3", "1");
		map.put("4", "1");
		
		System.out.println(map);
		System.out.println();

		map.forEach((k, v) -> {
			if (k.equals("2"))
				map.remove(k);
		});
		
		System.out.println();
		System.out.println(map);
	}
}
