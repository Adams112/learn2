package com.jzq.netty.chat3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KyroTest {
	public static void main(String[] args) {
		Kryo kryo = new Kryo();
		kryo.register(Message.class);
		
		Mess m = new Mess();
		m.setA(111);
		m.setStr("aaa");
		m.setStrs(null);
		m.setO("aaa");
		
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        Output output = new Output(byteArray);

        kryo.writeObjectOrNull(output, m ,m.getClass());
        output.flush();//待优化(AOP)
        output.close();

        byte[] objBytes = byteArray.toByteArray();
        System.out.println(Arrays.toString(objBytes));
        
        
        Input input = new Input(objBytes);
        Mess tObj =  kryo.readObjectOrNull(input, Mess.class);
        input.close();
        try {
            byteArray.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(tObj);
	}
	
	static class Mess {
		private int a;
		private String str;
		private Object o;
		private String[] strs;
		
		public int getA() {
			return a;
		}
		public void setA(int a) {
			this.a = a;
		}
		public String getStr() {
			return str;
		}
		public void setStr(String str) {
			this.str = str;
		}
		public Object getO() {
			return o;
		}
		public void setO(Object o) {
			this.o = o;
		}
		public String[] getStrs() {
			return strs;
		}
		public void setStrs(String[] strs) {
			this.strs = strs;
		}
		@Override
		public String toString() {
			return "Mess [a=" + a + ", str=" + str + ", o=" + o + ", strs=" + Arrays.toString(strs) + "]";
		}
		
		
	}
}
