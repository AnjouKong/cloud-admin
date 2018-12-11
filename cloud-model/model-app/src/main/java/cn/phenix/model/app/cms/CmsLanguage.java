package cn.phenix.model.app.cms;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "cms_language")
@PrimaryKeyJoinColumn(name = "id")
public class CmsLanguage extends CmsResource {

    /**
     * 风格
     */
    @Column(length = 50)
    private String style;

    /**
     * 相对父窗口位置（Y轴）
     */
    @Column(length = 100)
    private String parentTop;

    /**
     * 相对父窗口位置（X轴）
     */
    @Column(length = 100)
    private String parentLeft;

    //pms信息
    @Column(length = 1000)
    private String pms;

    //pms信息默认内容
    @Column(length = 1000)
    private String pmsDefault;

    //pms字体大小
    @Column
    private Integer fontSize = 24;

    //pms字体颜色
    @Column(length = 20)
    private String fontColor = "#ffffff";

    //pms信息相对父窗口位置（X轴）
    @Column(length = 100)
    private String pmsLeft;

    //pms信息相对父窗口位置（Y轴）
    @Column(length = 100)
    private String pmsTop;

    /**
     * 图片宽度
     */
    @Column(length = 100)
    private String width;
    /**
     * 图片高度
     */
    @Column(length = 100)
    private String height;

    @Transient
    private String[] languageDic;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.language);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.language);
    }

    public static final String modelName = "cn.phenix.model.app.cms.CmsLanguage";
//    public static final String fdkey_nofocus = "nofocus";
//    public static final String fdkey_focus = "focus";

}
