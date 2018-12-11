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
 * 媒资库
 * Created by fxq
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="MEDIA_RESOURCE")
public class NewMedia extends BaseModel {

    private static final long serialVersionUID = -3891085149196148484L;
    //第三方媒资库标识(方便数据核对)
    @Column
    private String programCode;

    //节目分集数(字符,兼容第三方媒资库)
    @Column
    private String programVolumnCount;
    //媒资库海报
    @Column
    private String programPictureUrl;

    @Column
    private String seriesId;
}
