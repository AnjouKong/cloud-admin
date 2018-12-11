package cn.phenix.model.app.media;

import cn.phenix.cloud.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 媒资专题详情
 */
@Getter
@Setter
@Entity
@Table(name = "APP_MEDIA_SUBJECT_DETAIL")
public class MediaSubjectDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String modelName="cn.phenix.model.app.media.MediaSubjectDetail";

    public static final String fdKey="mediaPic";
    /**
     * 媒资Id
     */
    @Column(length = 40)
    private String mediaSeriesId;

    /**
     * 顺序
     */
    @Column(length = 10)
    private Integer location;


    /**
     * 专题
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private MediaSubject mediaSubject;

    @Transient
    private String seriesName;
    @Transient
    private String seriesCode;
    @Transient
    private String cpName;
    @Transient
    private String releaseYear;
    @Transient
    private String subjectId;

}
