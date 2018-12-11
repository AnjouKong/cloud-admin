package cn.phenix.cloud;

import cn.phenix.cloud.core.utils.SpringContextHolder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * @author xiaobin
 * @create 2017-09-21 下午1:13
 **/
@SpringBootApplication
@EnableCaching
@EntityScan(basePackages = "cn.phenix.model")
//@Import({DynamicDataSourceRegister.class}) // 注册动态多数据源
/*@EnableJpaRepositories(repositoryBaseClass = GenericJpaRepositoryImpl.class,
        repositoryFactoryBeanClass = GenericJpaRepositoryFactoryBean.class,
        basePackages = "cn.phenix.cloud.admin")*/
public class AdminApplication {

    public static String TEMPLATE_PATH = "";

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }


    public static void main(String[] args) {
        if (args != null && args.length > 0)
            TEMPLATE_PATH = args[0];
        new SpringApplicationBuilder(AdminApplication.class).web(true).run(args);
    }
}
