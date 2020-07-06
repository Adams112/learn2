package webdemo.mvc;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AnnotationTestController {
	
	
	@RequestMapping("cookie")
	@ResponseBody
	public String cookie(@CookieValue("cookieName") String name) {
		return name;
	}
	
	@RequestMapping("body")
	@ResponseBody
	public String body(@RequestBody String name) {
		return name;
	}
	
	@RequestMapping("entity")
	@ResponseBody
	public String entity(HttpEntity<Rect> r) {
		return r.toString();
	}
	
	@RequestMapping("respEntity")
	public ResponseEntity<String> resp() {
		return ResponseEntity.ok().eTag("tag").body("body");
	}
}
