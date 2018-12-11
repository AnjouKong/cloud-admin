package cn.phenix.model.tenant.renter;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 商户场景
 */
@Getter
@Setter
@Entity
@Table(name = "t_tenant_scene")
public class TenantScene extends TenantBaseModel {

    /**
     * 场景模板Id
     */
    private String sceneId;

    /**
     * 场景名称
     */
    private String sceneName;

    /**
     * 对应的uiId
     */
    private String uiId;
    /**
     * 休眠时间
     */
    private String dormancyMinute;
    /**
     * 屏保时间
     */
    private String screenSaverMinute;

    //无法更改
    /**
     * 开启主题包
     */
    @Column(columnDefinition = "TINYINT", length = 4)
    private boolean openTheme;

    /**
     * 是否显示价格  0不显示，1显示
     */
    @Column(length = 10)
    private String showPrice;

    /**
     * vod首页Id
     */
    private String mediaHomePageId;

    /**
     * 适用分辨率
     */
    private String resolution;
    /**
     * 风格
     */
    private String style;
    /**
     * 适用比例
     */
    private String proportion;


    @Transient
    private String groupName;

    @Transient
    private String homePageName;

    @Transient
    private String templateName;
    @Transient
    private String templateLanguage;


    @Transient
    private String bootResourceArr; //开机

    @Transient
    private String shutdownResourceArr; //关机

    @Transient
    private String screenSaverResourceArr; //屏保
    @Transient
    private String musicResourceArr; //背景音乐

    /**
     * 扁平化风格
     */
    public static final String style_delayering = "delayering";
    /**
     * 悬浮风格
     */
    public static final String style_suspension = "suspension";

    public static final String modelName = "cn.phenix.model.tenant.renter.TenantScene";

    public static final String fdKey_backgroundMusic = "music";

}
