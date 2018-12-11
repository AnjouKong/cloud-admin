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
@Table(name = "cms_music")
@PrimaryKeyJoinColumn(name = "id")
public class CmsMusic extends CmsResource {


    @Transient
    private String isDisabled;

    @Transient
    private String[] languageDic;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.music);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.music);
    }

    public static final String modelName = "cn.phenix.model.app.cms.CmsMusic";

}
