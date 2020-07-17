package com.jzq.crypto.base64;

public class Base64Test {

	public static void main(String[] args) {
		String str = 
				"11月20日0点\r\n" + 
				"北京 小雨\r\n" + 
				"心情：好平静\r\n" + 
				"今天做了很多事情\r\n" + 
				"回到家里有点烦心\r\n" + 
				"接到她的电话\r\n" + 
				"我都没有使用好的语气\r\n" + 
				"Sorry，那不是我本意\r\n" + 
				"也不知道我最近是怎么搞的\r\n" + 
				"也许被那几只狗气的\r\n" + 
				"Saturday、Sunday没有时间考虑考虑休息\r\n" + 
				"它们等着我混音\r\n" + 
				"今日消费是281人民币\r\n" + 
				"落地窗上好像已经蒙上厚厚的雾气\r\n" + 
				"刚倒的一杯热水 凉了 像极了我和你\r\n" + 
				"天地虽然好冷 但房里没有开暖气\r\n" + 
				"要顺应自然规律 就像当时我放开你\r\n" + 
				"我调整心态 呼吸节奏渐渐的平缓\r\n" + 
				"把电视打开 节目和嘉宾全在瞎侃\r\n" + 
				"短信快存满 假意or真情 自知冷暖\r\n" + 
				"楼上女人哭喊 夫妻吵架没有人管\r\n" + 
				"最惨的事 不是忘不了悲伤的回忆\r\n" + 
				"而是 那些悲伤的 却已经开始记不起\r\n" + 
				"我渐渐丢失全部的线索所有的证据\r\n" + 
				"但我还记得我爱过你 所以我要谢谢你\r\n" + 
				"我调整心态 呼吸节奏渐渐的平缓\r\n" + 
				"把电视打开 节目和嘉宾还在瞎侃\r\n" + 
				"短信快存满 假意or真情 自知冷暖\r\n" + 
				"楼上女人哭喊 夫妻吵架没有人管\r\n" + 
				"我不相信爱 目前非常缺乏安全感\r\n" + 
				"蜷缩的心态 需要用一段时间舒展\r\n" + 
				"他们说的爱 如果有 我就为它平反\r\n" + 
				"江上两条红船 寒风斜雨中你摇摆";
		
		Base64Codec codec = null;
		String encoded = null, decoded = null;
		
		codec = Base64Codec.base64CodecBouncy;
		encoded = codec.encode(str);
		decoded = codec.decode(encoded);
		System.out.println(encoded);
		System.out.println(decoded);
		System.out.println(str.equals(decoded));
		
		codec = Base64Codec.base64CodecCommon;
		encoded = codec.encode(str);
		decoded = codec.decode(encoded);
		System.out.println(encoded);
		System.out.println(decoded);
		System.out.println(str.equals(decoded));
		
		codec = Base64Codec.urlBase64CodecBouncy;
		encoded = codec.encode(str);
		decoded = codec.decode(encoded);
		System.out.println(encoded);
		System.out.println(decoded);
		System.out.println(str.equals(decoded));
		
		codec = Base64Codec.urlBase64CodecCommon;
		encoded = codec.encode(str);
		decoded = codec.decode(encoded);
		System.out.println(encoded);
		System.out.println(decoded);
		System.out.println(str.equals(decoded));
	}

}
