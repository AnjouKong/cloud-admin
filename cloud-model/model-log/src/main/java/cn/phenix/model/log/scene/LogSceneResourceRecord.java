package cn.phenix.model.log.scene;


import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by fxq on 2016/12/29.
 */
@Getter
@Setter
@Entity
@Table(name = "log_scene_resource_record")
public class LogSceneResourceRecord extends BaseModel {


    /**
     *  资源Id
     */
    private String resourceId;

    /**
     *  资源类型
     */
    private String resourceType;

    /**
     * 终端Id
     */
    private String deviceId;

    /**
     * 房间号
     */
    private String roomId;

    /**
     * 商户Id
     */
    private String tenantId;

    /**
     * 商户名称
     */
    private String tenantName;

    /**
     *  资源投放位置
     */
    private String resourceFranchisePlace;

    /**
     * 曝光时间
     */
    private Date exposureTime;

    @Column(length = 10000)
    private String resourceJson;
}
