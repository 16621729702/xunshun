package com.wink.livemall.admin;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })

@ComponentScan(basePackages = {"com.wink.livemall.*"})
@MapperScan("com.wink.livemall.**.dao")
@EnableScheduling
public class Runner  {
	
 
	public static void main(String[] args) {
		SpringApplication.run(Runner.class, args);
		 
	}
	
	
}

