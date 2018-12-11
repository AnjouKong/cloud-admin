package cn.phenix.cloud.admin.app.scene.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "resource")
@Getter
@Setter
public class ResourceConf {

    private String bootResourceAPI;
    private String screenProtectorAPI;


}