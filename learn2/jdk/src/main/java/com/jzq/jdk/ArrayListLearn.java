package com.jzq.jdk;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ArrayListLearn {
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < 20; i++) {
			int cap = getCapacity(list);
			int size = list.size();
			System.out.println("cap: " + cap + ", size: " + size);
			list.add(1);
			list.trimToSize();
		}
	}
	
    public static int getCapacity(ArrayList<?> arrayList) {
        try {
            Field elementDataField = ArrayList.class.getDeclaredField("elementData");
            elementDataField.setAccessible(true);
            return ((Object[]) elementDataField.get(arrayList)).length;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
