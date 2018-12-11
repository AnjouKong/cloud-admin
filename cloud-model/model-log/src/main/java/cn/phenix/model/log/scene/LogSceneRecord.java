package cn.phenix.model.log.scene;


import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 场景拉取记录
 * Created by fxq on 2016/12/29.
 */
@Getter
@Setter
@Entity
@Table(name = "log_scene_record")
public class LogSceneRecord extends BaseModel {



    /**
     * 是否成功 0失败  1成功
     */
    @Column(length = 50)
    private boolean success;

    /**
     * 异常信息
     */
    @Column(length = 5000)
    private String errorMessage;

    /**
     * sceneId
     */
    @Column(length = 50)
    private String tenantSceneId;

    /**
     * uiId
     */
    @Column(length = 50)
    private String uiId;

    /**
     * 终端Id
     */
    @Column(length = 100)
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
