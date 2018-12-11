
package cn.phenix.model.app.cms;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


/**
 * Created by dwp on 2018/9/27.
 */

@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "cms_custom")
@PrimaryKeyJoinColumn(name = "id")
public class CmsCustom extends CmsResource {
    @Column(columnDefinition = "text")
    private String content;

    @Transient
    private String isDisabled;

    @Transient
    private String[] languageDic;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.custom);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.custom);
    }

    public static final String modelName = "cn.phenix.model.app.cms.CmsCustom";
}

