package cn.phenix.model.app.newMedia;

import cn.phenix.cloud.core.model.BaseModel;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * 媒资标签
 * Created by fxq
 */
@Getter
@Setter
@Entity
@Table(name = "MEDIA_TAG")
public class NewMediaTag extends BaseModel {

    public static final String modelName = "cn.phenix.model.app.newMedia.NewMediaTag";
    public static final String fdKey_static = "static";
    public static final String fdKey_focus = "focus";
    public static final String fdKey_selected = "selected";

    public NewMediaTag() {
    }

    public NewMediaTag(String id) {
        super();
        this.id = id;
    }

    private static final long serialVersionUID = 6015786810399165687L;
    @Column
    private String tagName;
    //发布状态
    @Column
    private boolean publish;
    //分类类型    媒资meidia    专题subject
    @Column(length = 100)
    private String type;

    /**
     * 排序
     */
    @Column(name = "index_")
//    @TableField("index_")
    private Integer index;

    @ManyToMany(mappedBy = "mediaTagsList", fetch = FetchType.LAZY)
    private List<NewMediaSeries> mediaSeriesList = Lists.newArrayList();

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "MEDIA_SUBJECT_TAG", joinColumns = {@JoinColumn(name = "tag_id", nullable = false, updatable = true)}
            , inverseJoinColumns = {@JoinColumn(name = "media_subject_id", nullable = false, updatable = true)})
    private List<NewMediaSubject> mediaSubjectList = Lists.newArrayList();

    @Transient
    private String[] subjectIds;
}
