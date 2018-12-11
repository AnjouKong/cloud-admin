package cn.phenix.model.log;

import cn.phenix.cloud.core.model.BaseIncrementIdModel;
import cn.phenix.cloud.core.model.BaseMongoModel;
import cn.phenix.event.EventEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

/**
 * @author xiaobin
 * @create 2017-09-21 上午11:58
 **/
@Getter
@Setter
@ToString
public class LogDeviceEvent extends BaseIncrementIdModel implements BaseMongoModel {

    private static final long serialVersionUID = -5845851004309111714L;

    //collectionName
    public static final String COLLECTION_NAME = "LOG_DEVICE_EVENT_V2";

    @Id
    private String id;

    private EventEnum eventEnum;

    //资源ID
    private String resourceId;

    //资源名称
    private String resourceName;

    //会话token
    private String token;
    //設備信息
    private DeviceInfo deviceInfo;

    //接口Id
    private String apiId;

    //进入时间
    private String startTime;
    //离开时间
    private String endTime;

    //事件描述
    private String eventDescribe;

    private String apiVersion;

    public void doToken(String token, DeviceInfo deviceInfo) {
        this.token = token;
        this.deviceInfo = deviceInfo;
    }
}
