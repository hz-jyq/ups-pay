package com.pgy.ups.pay.gateway;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Properties;

@ServletComponentScan
@SpringBootApplication(scanBasePackages = { "com.pgy.ups.**" },exclude = {
		DataSourceAutoConfiguration.class})
public class UpsPayGatewayApplication {

	private static final Logger logger = LoggerFactory.getLogger(UpsPayGatewayApplication.class);

	public static void main(String[] args) {
		logger.info("begin to start-up [ups-pay-gateway]");
		SpringApplication.run(UpsPayGatewayApplication.class, args);
		logger.info("start-up [ups-pay-gateway] success !!!");
	}

	@Primary
	@Bean(name="druidDataSource")
	@ConfigurationProperties(prefix = "druid")
	public DataSource getDataSource(){
		return DruidDataSourceBuilder.create().build();
	}




}

