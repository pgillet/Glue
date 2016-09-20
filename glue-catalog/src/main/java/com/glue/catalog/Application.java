package com.glue.catalog;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.glue.catalog.logging.WebSocketAppender;
import com.glue.catalog.logging.WebSocketOutputStream;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		
		Logger foo = createLoggerFor("foo", ctx);
		
		
		for (int i = 0; i < 100; i++) {
			foo.info("Foo " + i);
		}
	}
	
	
	private static Logger createLoggerFor(String string, ApplicationContext ctx) {
		
		
		WebSocketOutputStream outputStream = ctx.getBean(WebSocketOutputStream.class);
		
		WebSocketAppender wsAppender = new WebSocketAppender();
		
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();

        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple.setContext(lc);
        ple.start();
        
        wsAppender.setEncoder(ple);
        wsAppender.setOutputStream(outputStream);
        
        wsAppender.setContext(lc);
        wsAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(string);
        logger.addAppender(wsAppender);
        logger.setLevel(Level.DEBUG);
        logger.setAdditive(false); /* set to true if root should log too */

        return logger;
  }
}
