package com.jzq.velocity.index;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	
	@RequestMapping("/index")
	public ModelAndView index() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "jack");
		map.put("age", "23");
		map.put("message", "my name is ");
		
		System.out.println("12345");
		return new ModelAndView("index", map);
	}
}
