package com.gao.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 管理用户身份验证服务器应用程序
 *
 * @author gaopenglin
 * @date 2023/02/07
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.gao.shop.mapper"})
public class AdminUserAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminUserAuthServerApplication.class, args);
    }

}
