package cn.phenix.model.app.newMedia;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 设备规格对应功能列表
 *
 **/

@Getter
@Setter
@Entity
@Table(name="MEDIA_SERIES_FUNC")
@DynamicUpdate
@DynamicInsert
public class NewMediaSeriesFunc extends BaseModel {

    //功能名称
    @Column
    private String funcName;
    //功能归属
    @Column
    private String parentId;
    //功能id
    @Column
    private String funcId;
    //媒资id
    @Column
    private String seriesId;


}
