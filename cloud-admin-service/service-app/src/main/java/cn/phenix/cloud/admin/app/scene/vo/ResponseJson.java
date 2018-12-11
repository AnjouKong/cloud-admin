package cn.phenix.cloud.admin.app.scene.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单接口返回json
 **/
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseJson {

    private ResponseHeader head;

}
