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
		
		Mess m = new Mess();
		m.type = Type.A;;
		
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
		Type type;

		@Override
		public String toString() {
			return "Mess [type=" + type + "]";
		}
		
		
		
		
	}
	
	static enum Type {
		A, B, C;
	}
}
