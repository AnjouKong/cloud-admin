package cn.phenix.model.tenant.device;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


/**
 *  设备应用
 */
@Getter
@Setter
@Entity
@Table(name="T_DEVICE_USER_APPLICATION")
@DynamicUpdate
@DynamicInsert
public class DeviceApplication extends BaseModel implements Serializable {

    public static  final  String modelName="cn.phenix.model.tenant.device.DeviceApplication";

    private static final long serialVersionUID = -4037654712154776090L;

    //名称
    private String applicationName;


}
