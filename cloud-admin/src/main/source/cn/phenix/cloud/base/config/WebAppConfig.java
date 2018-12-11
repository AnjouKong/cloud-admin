package cn.phenix.cloud.base.config;

import cn.phenix.cloud.base.interceptor.WebInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author xiaobin
 * @create 2017-09-25 下午5:10
 **/
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }

    /*@Bean
    public CommonsMultipartResolver commonsMultipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxInMemorySize(102400);
        //100M=100*1024*1024(B)=10485760B
        commonsMultipartResolver.setMaxUploadSize(104857600L);
        commonsMultipartResolver.setMaxUploadSizePerFile(104857600L);
        commonsMultipartResolver.setResolveLazily(true);
        return commonsMultipartResolver;
    }*/

    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        //addInterceptor.excludePathPatterns("/error");
        //addInterceptor.excludePathPatterns("/swagger**");
        //addInterceptor.addPathPatterns("/**");
        registry.addInterceptor(new WebInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
