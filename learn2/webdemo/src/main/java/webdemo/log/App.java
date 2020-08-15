package webdemo.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App {
public static void main(String[] args) throws Exception {
	ApplicationContext context = SpringApplication.run(App.class, args);
	context.getBean(LogTest.class).log();
}

}