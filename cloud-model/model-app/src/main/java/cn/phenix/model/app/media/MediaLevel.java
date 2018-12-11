package cn.phenix.model.app.media;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 媒资级别
 * Created by fxq on 2017/10/17.
 */
@Getter
@Setter
@Entity
@Table(name="APP_MEDIA_LEVEL")
public class MediaLevel extends BaseModel {

    private static final long serialVersionUID = 6015786810399165687L;
    @Column
    private String levelName;
    //状态 0启用  1停用
    @Column
    private String status;


}
