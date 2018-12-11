package cn.phenix.cloud.rpc.cache.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单接口返回json
 **/
@Getter
@Setter
@ApiModel("订单接口返回相关")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseJson {

    private ResponseHeader head;

}
