
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
@Table(name = "cms_secondary_page")
@PrimaryKeyJoinColumn(name = "id")
public class CmsSecondaryPage extends CmsResource {
    @Column(length = 50)
    private String style;

    @Column(columnDefinition = "text")
    private String content;

    @Transient
    private String isDisabled;

    @Transient
    private String[] languageDic;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.secondary);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.secondary);
    }

    public static final String modelName = "cn.phenix.model.app.cms.CmsSecondaryPage";
}

