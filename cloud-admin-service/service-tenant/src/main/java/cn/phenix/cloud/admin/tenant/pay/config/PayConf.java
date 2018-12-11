package cn.phenix.cloud.admin.tenant.pay.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pay")
@Getter
@Setter
public class PayConf {
    //退款地址
    private String refundUrl;

}