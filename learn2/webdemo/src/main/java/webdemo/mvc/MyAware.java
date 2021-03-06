package webdemo.mvc;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class MyAware implements ApplicationContextAware {
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println(applicationContext);
		
		Map<String, Object> beans = applicationContext.getBeansOfType(Object.class);
		beans.forEach((k, v) -> {
			System.out.println(k + "====" + v);
		});
	}

}
