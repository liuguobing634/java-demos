package lew.bing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by 刘国兵 on 2017/9/29.
 */

@SpringBootApplication
@EnableWebMvc
@ComponentScan(basePackages = "lew.bing.web")
@ImportResource("classpath:dubbo.xml")
public class BootStrap {

    public static void main(String[] args) {
        SpringApplication.run(BootStrap.class, args);
    }


}
