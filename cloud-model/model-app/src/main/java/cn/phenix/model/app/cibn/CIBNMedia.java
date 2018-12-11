package cn.phenix.model.app.cibn;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 媒资库
 */

@Getter
@Setter
@Entity
@Table(name = "APP_CIBN_RESOURCE")
public class CIBNMedia extends BaseModel {

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
