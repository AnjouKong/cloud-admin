package cn.phenix.model.app.scene;

import cn.phenix.cloud.core.model.BaseModel;
import cn.phenix.cloud.core.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 *  场景版本信息
 */

@Getter
@Setter
@Entity
@Table(name = "scene_version_info")
public class SceneVersionInfo extends BaseModel {

    /**
     * 场景基本信息Id
     */
    private String basicId;
    /**
     *  mongodbId（UI信息）
     */
    private String uiId;

    /**
     * 版本
     */
    private Integer version;

    /**
     *  内容标识
     */
    private Integer hashcode;

    /**
     * 发布状态
     */
    private String publish;

    /**
     * 未发布
     */
    public static final String unpublished="unpublished";
    /**
     * 已发布
     */
    public static final String published="published";


    @Transient
    public String getUpdateDateStr(){
        return DateUtils.format(this.getUpdateDate(),"yyyy-MM-dd HH:mm:ss");
    }
}
