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
@Table(name = "cms_imgCollection")
@PrimaryKeyJoinColumn(name = "id")
public class CmsImgCollection extends CmsResource {

    //切换类型
    private String switchType;

    @Transient
    private List<String> picUrl;

    @Transient
    private String[] languageDic;

    @Transient
    private String isDisabled;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.imgCollection);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.imgCollection);
    }

    public static final String modelName = "cn.phenix.model.app.cms.CmsImgCollection";
    /**
     * 缩略图
     */
    public static final String switchType_thumbnail = "thumbnail";
    /**
     * 页码
     */
    public static final String switchType_pagination = "pagination";

}
