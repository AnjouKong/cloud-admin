package cn.phenix.model.app.cms;


import cn.phenix.cloud.core.utils.DateUtils;
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
@Table(name = "cms_html")
@PrimaryKeyJoinColumn(name = "id")
public class CmsHtml extends CmsResource {

    @Transient
    private String[] languageDic;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.html);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.html);
    }

    public static final String modelName = "cn.phenix.model.app.cms.CmsHtml";

    public static final String fdKey = "css";

    @Transient
    private CmsChannel channel;

    @Transient
    private String isDisabled;

    @Transient
    public String getUpdateDateStr(){
        return DateUtils.format(this.getUpdateDate(),"yyyy-MM-dd HH:mm:ss");
    }

}
