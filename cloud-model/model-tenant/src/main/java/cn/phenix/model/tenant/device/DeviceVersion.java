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
 *
 */
@Getter
@Setter
@Entity
@Table(name="T_DEVICE_USER_VERSION")
@DynamicUpdate
@DynamicInsert
public class DeviceVersion extends BaseModel implements Serializable {


    private static final long serialVersionUID = -4037654712154776090L;

    //应用ID
    private String applicationId;

    //策略名称
    private String tacticsName;

    //策略类型  地区area  时间time  商家tenant 上一版本lastVersion
    private String tactics;

    //策略对应值  地区code 时间 开始时间  商家id  上一版本id
    private String tacticsFirstValue;

    //策略对应值 2  只有策略为时间时，才会使用此字段存储结束时间
    private String tacticsSecondValue;

    //是否强制更新
    private boolean forceUpdate;

    /**
     * 地区
     */
    public static final String area = "area";

    /**
     * 时间
     */
    public static final String time = "time";

    /**
     * 商家
     */
    public static final String tenant = "tenant";

    /**
     * 上一版本
     */
    public static final String lastVersion = "lastVersion";

}
