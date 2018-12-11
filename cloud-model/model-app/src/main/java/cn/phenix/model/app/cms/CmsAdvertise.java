package cn.phenix.model.app.cms;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "cms_advertise")
public class CmsAdvertise extends CmsResource {

    /**
     * 投放商
     */
    private String advFranchiser;
    /**
     * 类型  video、img
     */
    private String fileType;
    /**
     * 价格
     */
    @Column(precision = 32, scale = 2)
    private BigDecimal charge;

    //图片切换类型
    private String switchType;

    @Transient
    private String isDisabled;
    @Transient
    private String resourceUrl;
    @Transient
    private String[] languageDic;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.setResourceType(ResourceEnum.ad);
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        this.setResourceType(ResourceEnum.ad);
    }

    public static final String modelName = "cn.phenix.model.app.cms.CmsAdvertise";

}
