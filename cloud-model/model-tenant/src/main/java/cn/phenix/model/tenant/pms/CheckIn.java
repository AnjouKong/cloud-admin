package cn.phenix.model.tenant.pms;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author xiaobin
 * @create 2017-09-20 上午11:16
 **/
@Table(name = "T_TENANT_PMS_CHECK_IN",schema = "phenix_cloud_extend")
@Entity
@Getter
@Setter
public class CheckIn extends BaseModel implements Serializable{


    private String requestJson;

    private String requestIp;

    private String pmsId;
    private String tenantId;
    private String orgId;
    private String tenantName;
    private String roomNo;
    protected String roomTypeName;
    protected String roomBuildingName;
    private String roomBuildingCode;
    private String roomFloorName;
    private String roomFloorCode;
    private String checkInTime;
    private String checkOutTime;

}
