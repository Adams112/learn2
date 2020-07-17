package webdemo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogTest {
	private static final Logger logger = LoggerFactory.getLogger(LogTest.class);
	
	public void log() {
		logger.info("log content from logTest");
		logger.info("class: {}", logger.getClass().toString());
	}
}
