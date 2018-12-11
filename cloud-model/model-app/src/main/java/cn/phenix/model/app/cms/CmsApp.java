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
@Table(name = "cms_app")
@PrimaryKeyJoinColumn(name = "id")
public class CmsApp extends CmsResource {

    /**
     * 动作  终端定义
     */
    private String action;
    /**
     * 包名  终端定义
     */

    /**
     * 参数
     */
    @Column(length =1000)

    private String param;
    private String packageName;
    @Transient
    private String isDisabled;
    @Transient
    private String[] languageDic;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.app);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.app);
    }


    public static final String modelName = "cn.phenix.model.app.cms.CmsApp";

}
