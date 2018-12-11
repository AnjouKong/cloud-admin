package cn.phenix.model.app.scene;

import cn.phenix.cloud.core.model.BaseModel;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;


/**
 * 场景基本信息
 */

@Getter
@Setter
@Entity
@Table(name = "scene_basic_info_template")
public class SceneBasicInfo extends BaseModel {

    public static final String modelName = "cn.phenix.model.app.scene.SceneBasicInfo";
    /**
     * 分类Id
     */
    private String categoryId;
    /**
     * 场景名称
     */
    private String sceneName;
    /**
     * 适用分辨率
     */
    private String resolution;
    /**
     * 风格(区分版本)
     */
    private String style;
    /**
     * 适用比例
     */
    private String proportion;

    /**
     * vod首页Id
     */
    private String mediaHomePageId;

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
     * 休眠时间
     */
    private String dormancyMinute;
    /**
     * 屏保时间
     */
    private String screenSaverMinute;

    @Transient
    private String homePageName;

    @Transient
    private String bootResourceArr; //开机

    @Transient
    private String shutdownResourceArr; //关机

    @Transient
    private String screenSaverResourceArr; //屏保

    @Transient
    private String musicResourceArr; //背景音乐

    @Transient
    private String themePackageResourceArr; //主题包
    @Transient
    private List<String> urlList = Lists.newArrayList();

    @Transient
    private String[] languageDic;

    @Transient
    private String[] readerId;
    @Transient
    private String[] editorId;
    @Transient
    private String auth;


    @Transient
    private boolean isPhysicalBootResource;

    /**
     * 扁平化风格
     */
    public static final String style_delayering = "delayering";
    /**
     * 悬浮风格
     */
    public static final String style_suspension = "suspension";


    public static final String fdKey_backgroundMusic = "music";
}
