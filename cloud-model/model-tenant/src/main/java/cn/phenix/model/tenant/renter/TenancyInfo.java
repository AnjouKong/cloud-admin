package cn.phenix.model.tenant.renter;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 租户详情的描述
 * 描述租户的数据范围、应用权限、
 * Created by xiaobin on 2017/8/14.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="T_TENANCY_EXPANSION")
public class TenancyInfo extends TenantBaseModel {

    private static final long serialVersionUID = 696856009114954305L;


    @Column
    private Boolean enablePms;//启用PMS
    @Column
    private String pmsName;//对接的pms厂商
    @Column
    private String pmsUrl;//PMS服务地址
    @Column
    private String appId;
    @Column
    private String appSecret; //混淆码
    @Column
    private String appSwitch; //应用开关（是否允许此商户升级应用）0否，1是
    //2018年3月5日16:50:59 是否开启日志上报 0否1是
    @Column(length = 1)
    private String logOn;
}
