package cn.phenix.cloud.rpc.cache.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单接口返回json
 **/
@Getter
@Setter
@ApiModel("订单接口返回相关")
public class ResponseHeader {

    private String bodyType;
    private int code;
    private String msg;

}
