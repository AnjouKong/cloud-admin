package cn.phenix.model.app.media;

import cn.phenix.cloud.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import java.util.List;

/**
 * 媒资专题
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "APP_MEDIA_SUBJECT")
public class MediaSubject extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String modelName = "cn.phenix.model.app.media.MediaSubject";
    public static final String fdKey_cover = "cover";//封面
    public static final String fdKey_backgroundPic = "backgroundPic"; //背景图
    public static final String fdKey_buy = "buy"; //购买背景图
    /**
     * 专题名称
     */
    @Column(length = 100)
    private String subjectName;

    /**
     * 购买按钮名称
     */
    @Column(length = 100)
    private String buyButtonName;

    /**
     * 是否显示购买按钮  0不显示  1显示
     */
    @Column(length = 100)
    private String showBuyButton;

    /**
     * 媒资列表
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @OrderBy(clause = "location asc")
    private List<MediaSubjectDetail> mediaList = Lists.newArrayList();

    @ManyToMany(mappedBy = "mediaSubjectList", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MediaTag> mediaTagsList = Lists.newArrayList();

    @Transient
    private String mediaArr;

    public MediaSubject() {
    }

    public MediaSubject(String id) {
        super();
        this.id = id;
    }
}
