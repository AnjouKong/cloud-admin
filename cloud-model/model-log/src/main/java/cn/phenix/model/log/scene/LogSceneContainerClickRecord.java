package cn.phenix.model.log.scene;


import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 容器点击记录
 * Created by fxq on 2018/1/26.
 */
@Getter
@Setter
@Entity
@Table(name = "log_scene_containerClick_record")
public class LogSceneContainerClickRecord extends BaseModel {

    /**
     * 容器名称
     */
    @Column(length = 100)
    private String containerName;

    /**
     * 容器事件类型（eventId）
     */
    @Column(length = 50)
    private String containerEventType;

    /**
     * 容器事件内容(json)
     */
    @Column(length = 5000)
    private String containerEventContent;

    /**
     * tenantSceneId
     */
    @Column(length = 50)
    private String tenantSceneId;


    /**
     * 终端Id
     */
    @Column(length = 50)
    private String deviceId;

    /**
     * 房间号
     */
    @Column(length = 50)
    private String roomId;

    /**
     * 商户Id
     */
    @Column(length = 50)
    private String tenantId;

    /**
     * 商户名称
     */
    @Column(length = 100)
    private String tenantName;


}
