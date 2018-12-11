package cn.phenix.cloud.base.config.view;

import cn.phenix.cloud.AdminApplication;
import cn.phenix.cloud.core.utils.Exceptions;
import cn.phenix.cloud.utils.FreeMarkers;
import org.apache.commons.lang3.StringUtils;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import java.util.Properties;

/**
 * @author xiaobin
 * @create 2017-09-25 下午3:33
 **/
@Configuration
public class BeetlViewConfig {

    @Bean(initMethod = "init", name = "beetlConfig")
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
        BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
        try {
            // WebAppResourceLoader 配置root路径是关键
            if(StringUtils.isNotBlank(AdminApplication.TEMPLATE_PATH)){
                FileResourceLoader fileResourceLoader = new FileResourceLoader(AdminApplication.TEMPLATE_PATH);
                fileResourceLoader.setAutoCheck(true);
                beetlGroupUtilConfiguration.setResourceLoader(fileResourceLoader);
            }else{
                ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader("templates");
                classpathResourceLoader.setAutoCheck(true);
                beetlGroupUtilConfiguration.setResourceLoader(classpathResourceLoader);
            }
            //
            FreeMarkers.builder();

            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("beetl.properties");
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            beetlGroupUtilConfiguration.setConfigProperties(properties);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
        //读取配置文件信息
        return beetlGroupUtilConfiguration;
    }

    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
        beetlSpringViewResolver.setPrefix("/");
        beetlSpringViewResolver.setSuffix(".html");
        beetlSpringViewResolver.setCache(false);
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setOrder(0);
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        return beetlSpringViewResolver;
    }
}
