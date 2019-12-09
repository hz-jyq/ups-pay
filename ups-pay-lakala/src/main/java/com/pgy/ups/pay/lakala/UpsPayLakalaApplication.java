package com.pgy.ups.pay.lakala;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;


@ServletComponentScan
@SpringBootApplication(scanBasePackages = { "com.pgy.ups.**" },exclude = { DataSourceAutoConfiguration.class })
public class UpsPayLakalaApplication {

	
	private static final Logger logger = LoggerFactory.getLogger(UpsPayLakalaApplication.class);

	public static void main(String[] args) {
		logger.info("begin to start-up [ups-pay-lakala]");
		SpringApplication.run(UpsPayLakalaApplication.class, args);
		logger.info("start-up [ups-pay-lakala] success !!!");
	}

	
}




