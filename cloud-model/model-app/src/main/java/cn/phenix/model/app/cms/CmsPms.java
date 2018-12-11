package cn.phenix.model.app.cms;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by fxq on 2016/7/18.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "cms_pms")
@PrimaryKeyJoinColumn(name = "id")
public class CmsPms extends CmsResource {

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
    @Column
    private Integer fontSize = 24;
    @Column(length = 20)
    private String fontColor = "#ffffff";
    @Column(length = 500)
    private String defaultStatement;
    @Column(length = 500)
    private String statement;
    @Transient
    private String[] languageDic;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.pms);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.pms);
    }

    public static final String modelName = "cn.phenix.model.app.cms.CmsPms";

}
