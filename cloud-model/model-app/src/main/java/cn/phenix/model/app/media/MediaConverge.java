package cn.phenix.model.app.media;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 媒资聚合元数据
 *
 **/

@Getter
@Setter
@Entity
@Table(name="APP_MEDIA_CONVERGE_MAIN")
@DynamicUpdate
@DynamicInsert
public class MediaConverge extends BaseModel {

    //聚合id：用于标记同一媒资
    //主媒资的mergeId=seriesId，
    @Column
    private String mergeId;
    //规则id
    @Column
    private String ruleCode;
    //功能名称
    @Column
    private String funcName;

    //功能id
    @Column
    private String funcId;
    //媒资id
    @Column
    private String seriesId;

    //媒资名称
    @Column
    private String seriesNames;

}
