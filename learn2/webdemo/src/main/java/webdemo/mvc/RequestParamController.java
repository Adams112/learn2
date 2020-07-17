package webdemo.mvc;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RequestParamController {

	// 获取查询参数、表单、url-encoded表单
	@RequestMapping("/param")
	@ResponseBody
	public String func(@RequestParam("name") String name, @RequestHeader("Content-Type") String type) {
		System.out.println(type);
		return name;
	}
	
	// 获取所有查询参数
	@RequestMapping("/param2")
	@ResponseBody
	public String func2(@RequestParam Map<String, String> map) {
		System.out.println(map);
		return "ok";
	}
	
	// 处理数组
	@RequestMapping("/param3")
	@ResponseBody
	public String func3(@RequestParam("name") String[] str) {
		// String str, List<String> str
		System.out.println(Arrays.toString(str));
		return "ok";
	}
	
	// 实体类不需要注解，也需要为查询参数或表单
	@RequestMapping("/param4")
	@ResponseBody
	public String func4(Rect r) {
		System.out.println(r);
		return "ok";
	}
}
