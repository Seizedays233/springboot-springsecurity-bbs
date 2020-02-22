package com.seizedays.ideasharingprovider;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
@MapperScan(basePackages = "com.seizedays.ideasharingprovider.mappers")
public class IdeasharingProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdeasharingProviderApplication.class, args);
	}

}
