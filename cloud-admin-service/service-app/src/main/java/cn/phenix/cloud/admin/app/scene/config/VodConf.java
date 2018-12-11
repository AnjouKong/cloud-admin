package cn.phenix.cloud.admin.app.scene.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vod")
@Getter
@Setter
public class VodConf {

    private String deviceId;
    private String getTokenUrl;
    private String CategoryHandler;
    private String SearchHandler;
    private String MediaDetailController;

}