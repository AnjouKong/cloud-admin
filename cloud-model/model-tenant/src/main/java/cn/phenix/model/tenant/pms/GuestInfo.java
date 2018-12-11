package cn.phenix.model.tenant.pms;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author xiaobin
 * @create 2017-09-20 上午11:20
 **/
@Table(name = "T_TENANT_PMS_GUEST",schema = "phenix_cloud_extend")
@Entity
@Getter
@Setter
public class GuestInfo extends BaseModel implements Serializable {


    /**
     * 默认CheckIn，1：CheckOut
     */
    private String checkStatus;
    private String countryCode;

    private String provinceCode;

    private String salutation;

    private String cityCode;
    private String address;


    private String checkInId;

    private String guestId;

    private String guestName;

    private String idCardNo;

    private String idCardTypeId;

    private String nationality;

    private String gender;

    private String birthday;

    private String tenantId;




}
