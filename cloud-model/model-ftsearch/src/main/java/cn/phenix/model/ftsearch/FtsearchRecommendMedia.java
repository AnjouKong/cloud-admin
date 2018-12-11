package cn.phenix.model.ftsearch;

import cn.phenix.cloud.core.model.BaseModel;
import cn.phenix.model.app.media.MediaSeries;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 相关推荐
 *
 * @author xiaobin
 * @create 2018-03-27 下午1:41
 **/
@Setter
@Getter
@Table(name = "ftsearch_recommend_media")
@Entity
@DynamicInsert
@DynamicUpdate
public class FtsearchRecommendMedia extends BaseModel implements Serializable {

    //级别ID
    @Column(length = 32)
    private String levelId;

    //媒资
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mediaId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private MediaSeries mediaSeries;

    //排序
    @Column(length = 32)
    protected Integer recommendOrder;
}
