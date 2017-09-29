package lew.bing.test;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Created by 刘国兵 on 2017/9/29.
 */
@ContextConfiguration()
@ComponentScan("lew.bing")
@EnableJpaRepositories(basePackages = "lew.bing")
@EntityScan(basePackages = "lew.bing.domain")
@EnableTransactionManagement
public class TestConfig {



}
