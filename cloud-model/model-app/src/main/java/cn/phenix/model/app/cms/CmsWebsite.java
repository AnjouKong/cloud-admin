package cn.phenix.model.app.cms;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * Created by fxq on 2018/5/24.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "cms_website")
@PrimaryKeyJoinColumn(name = "id")
public class CmsWebsite extends CmsResource {

    private String url;

    /**
     * 网站类型  内部网站 internal   外部网站 external
     */
    @Column(length = 20)
    private String type;

    //参数     {token}，{language}

    @Transient
    private List<String> picUrl;

    @Transient
    private String isDisabled;
    @Transient
    private String[] languageDic;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.website);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.website);
    }

    public static final String modelName = "cn.phenix.model.app.cms.CmsWebsite";

}
