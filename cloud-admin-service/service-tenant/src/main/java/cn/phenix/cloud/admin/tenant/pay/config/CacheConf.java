package cn.phenix.cloud.admin.tenant.pay.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cache")
@Getter
@Setter
public class CacheConf {
    //清除缓存地址
    private String cacheUrl;

}