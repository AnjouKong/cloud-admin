
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
/*@DynamicUpdate
@DynamicInsert*/
@Table(name = "cms_theme_package")
@PrimaryKeyJoinColumn(name = "id")
public class CmsThemePackage extends CmsResource {
    @Column
    private String flag;

    @Column
    private String version;

    @Transient
    private String isDisabled;

    @Transient
    private String[] languageDic;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.theme);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.theme);
    }

    public static final String modelName = "cn.phenix.model.app.cms.CmsThemePackage";
}

