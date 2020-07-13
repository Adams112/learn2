package webdemo.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

	@ResponseBody
	@RequestMapping("/hello/**")
	public String func() {
		return "hello from /hello/**";
	}

	@ResponseBody
	@RequestMapping("/hello/*")
	public String func2() {
		return "hello from /hello/*";
	}

	@ResponseBody
	@RequestMapping("/hello")
	public String func3() {
		return "hello from /hello";
	}

	@ResponseBody
	@RequestMapping(path="/hello", consumes="application/json")
	public String func4() {
		return "hello from /hello consumes";
	}
	
	@ResponseBody
	@RequestMapping(path="/hello", produces="application/json")
	public String func5() {
		return "hello from /hello produces";
	}
	
	@ResponseBody
	@RequestMapping(path="/hello", headers= "a=b")
	public String func6() {
		return "hello from /hello headers";
	}
	
	@ResponseBody
	@RequestMapping(path="/hello", params= "a=b")
	public String func7() {
		return "hello from /hello params";
	}
	
	@RequestMapping("/null")
	public String func8(@RequestParam String name) {
		System.out.println(name);
		return null;
	}
}
