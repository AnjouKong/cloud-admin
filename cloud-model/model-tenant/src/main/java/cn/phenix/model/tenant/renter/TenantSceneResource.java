package cn.phenix.model.tenant.renter;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 场景广告信息
 */

@Getter
@Setter
@Entity
@Table(name = "t_tenant_scene_resource")
public class TenantSceneResource extends BaseModel {
    /**
     * 商户场景Id
     */
    private String tenantSceneId;
    /**
     * 广告Id
     */
    private String resourceId;
    /**
     * 事件类型    开机事件 boot、屏保事件 screenSaver 、关机事件 shutdown
     */
    private String eventType;
    /**
     * 可跳转时间(用于广告事件)
     */
    private String skipTime;
    /**
     * 单个资源播放时间
     */
    private String playTime;
    /**
     * 是否向下授权 0否 1是
     */
    private boolean isAuthorize;
    /**
     * 是否在开机页面播放
     */
    @Column(columnDefinition = "TINYINT", length = 1)
    private boolean isBootPlay;
    /**
     * 是否在launcher页面播放
     */
    @Column(columnDefinition = "TINYINT", length = 1)
    private boolean isLauncherPlay;
    /**
     * 是否允许强制跳过  0允许 1禁止 (用于广告事件)
     */
    @Column(columnDefinition = "TINYINT", length = 1)
    private boolean skip;
    /**
     * 是否为物理开机资源  0否 1是 (一个场景中只存在一条为1的数据)
     */
    @Column(columnDefinition = "TINYINT", length = 1)
    private boolean isPhysicalBootResource;
    /**
     * 轮播  0否 1是
     */
    @Column(columnDefinition = "TINYINT", length = 4)
    private boolean carousel;
    /**
     * 排序字段  升序排列
     */
    @Column(name = "index_")
    private int index;
    /**
     * 是否继承模板  0否 1是
     */
    private boolean succeed;

    /**
     * 屏保事件
     */
    public static final String screenSaverEvent = "screenSaver";

    /**
     * 开机事件
     */
    public static final String bootEvent = "boot";

    /**
     * 关机事件
     */
    public static final String shutdownEvent = "shutdown";

    /**
     * 背景音乐
     */
    public static final String backgroundMusic = "backgroundMusic";

    @Transient
    private String languageName;
    @Transient
    private String resource;
    @Transient
    private String resourceName;
    @Transient
    private String resourceType;
    @Transient
    private boolean isVideo;
}
