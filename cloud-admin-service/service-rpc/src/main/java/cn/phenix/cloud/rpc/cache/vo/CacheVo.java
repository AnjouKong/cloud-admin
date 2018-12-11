package cn.phenix.cloud.rpc.cache.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 订单退款相关
 *
 * @author
 * @create
 **/
@Getter
@Setter
@ToString
@ApiModel("清除商户缓存")
public class CacheVo {

    @ApiModelProperty("系统授权的服务标识")
    private String appId;

    @ApiModelProperty("时间戳")
    private String timestamp;

    @ApiModelProperty("认证码")
    private String signature;

    @ApiModelProperty("商户id")
    private String tenantId;

}
