package com.tinatest.line_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@SpringBootApplication(exclude = JmxAutoConfiguration.class)
@EnableScheduling
public class LineBotApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LineBotApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(LineBotApplication.class, args);
		System.out.println("LineageKing Bot Start........");
	}

}
