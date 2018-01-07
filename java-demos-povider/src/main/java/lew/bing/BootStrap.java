package lew.bing;

import com.alibaba.dubbo.config.spring.schema.DubboBeanDefinitionParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by 刘国兵 on 2017/9/29.
 */
@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackages = "lew.bing.domain")
@EnableTransactionManagement
@ImportResource("classpath:dubbo.xml")
public class BootStrap {

    public static void main(String[] args) throws IOException {
        System.out.println("hello");
        ApplicationContext context = SpringApplication.run(BootStrap.class, args);
        System.out.println("任意键退出");
        new BufferedInputStream(System.in).read();
    }

}
