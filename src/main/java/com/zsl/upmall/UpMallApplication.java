package com.zsl.upmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.zsl.upmall.mapper")
public class UpMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpMallApplication.class, args);
    }

}
