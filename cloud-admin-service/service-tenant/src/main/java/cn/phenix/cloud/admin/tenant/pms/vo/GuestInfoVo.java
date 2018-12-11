package cn.phenix.cloud.admin.tenant.pms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * pms相关
 *
 **/
@Getter
@Setter
@ApiModel("PMS相关")
public class GuestInfoVo {

    @ApiModelProperty("guestId")
    private String id;

    @ApiModelProperty("商户名称")
    private String tenantName;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("旅客名称")
    private String guestName;

    @ApiModelProperty("入住时间")
    private String checkInTime;

    @ApiModelProperty("退房时间")
    private String checkOutTime;





}
