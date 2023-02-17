package con.gao.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 网关服务器应用程序
 *
 * @author gaopenglin
 * @date 2023/02/07
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("con.gao")
public class GateWayServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayServerApplication.class, args);
    }

}
