package cn.phenix.model.app.cms;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "cms_img")
@PrimaryKeyJoinColumn(name = "id")
public class CmsImg extends CmsResource {

    @Transient
    private List<String> picUrl;

    @Transient
    private String isDisabled;

    @Transient
    private String[] languageDic;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.img);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.img);
    }

    public static final String modelName = "cn.phenix.model.app.cms.CmsImg";

}
