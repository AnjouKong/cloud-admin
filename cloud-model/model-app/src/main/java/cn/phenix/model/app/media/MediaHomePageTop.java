package cn.phenix.model.app.media;

import cn.phenix.cloud.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 媒资首页顶部
 */
@Getter
@Setter
@Entity
@Table(name = "APP_MEDIA_HOMEPAGE_TOP")
public class MediaHomePageTop extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String modelName="cn.phenix.model.app.media.MediaHomePageTop";

    public static final String fdk_img="img";
    public static final String fdk_focusImg="focusImg";

    /**
     * 资源Id
     */
    @Column(length = 40)
    private String resourceId;

    /**
     * 资源类型 detail  subject
     */
    @Column(length = 40)
    private String resourceType;

    /**
     * 顺序
     */
    @Column(length = 10)
    private Integer location;

    /**
     * 首页
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homePageId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private MediaHomePage mediaHomePage;

    @Transient
    private String name;

    @Transient
    private String homePageId;

}
