package cn.phenix.model.app.cms;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 资源
 *
 * @author xiaobin
 * @create 2018-01-10 下午5:54
 **/
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "cms_resource")
@Inheritance(strategy = InheritanceType.JOINED)
public class CmsResource extends BaseModel {

    @Column(length = 100)
    private String name;

    @Column(length = 200)
    private String callbackUrl;

    @Column(length = 32, nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceEnum resourceType;

    @Column(length = 100)
    private String channelId;

    @Column(length = 10)
    private boolean disabled;

    @Transient
    private String languageName;
    @Transient
    private String resourceTypeText;
    @Transient
    private String isDisabled;
    @Transient
    private String resource;
    @Transient
    private boolean isVideo;

    /**
     * 语言资源fdKey
     */
    public static final String fdKey_language = "pic";

    /**
     * pms资源fdKey
     */
    public static final String fdKey_pms = "pic";

}
