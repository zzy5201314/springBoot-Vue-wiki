package com.zzy.wiki.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@ComponentScan("com.zzy")
@MapperScan("com.zzy.wiki.mapper")  //配置需要扫描的mapper包，不是xml包
//@ComponentScan({"com.zzy","com.test"})		扫描多个包的写法
@EnableScheduling
@EnableAsync
public class WikiApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(WikiApplication.class);
		Environment env = app.run(args).getEnvironment();
		log.info("启动成功！！");
		log.info("地址: \thttp://127.0.0.1:{}",env.getProperty("server.port"));
	}

}
