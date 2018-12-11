package cn.phenix.model.app.cms;


import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "cms_channel")
public class CmsChannel extends BaseModel {
    private static final long serialVersionUID = 1L;


    @Column
//    @Comment("父级ID")
    private String parentId;

    @Column
//    @Comment("栏目名称")
    private String name;

    @Column
//    @Comment("栏目类型")
    private String type;


    @Column
//    @Comment("是否禁用")
    private boolean disabled;

    @Column
//    @Comment("排序字段")
    private Integer location;

    @Column
//    @Comment("有子节点")
    private boolean hasChildren;

    /**
     * 类型  图片
     */
    public static final String img = "img";
    /**
     * 类型  图片集
     */
    public static final String imgCollection = "imgCollection";

    /**
     * 类型  APP
     */
    public static final String app = "app";

    /**
     * 类型  html
     */
    public static final String html = "html";
    /**
     * 类型  视频
     */
    public static final String video = "video";
    /**
     * 类型  视频集
     */
    public static final String videoList = "videoList";
    /**
     * 类型  语言
     */
    public static final String language = "language";

    /**
     * 类型  pms
     */
    public static final String pms = "pms";
    /**
     * 类型  音乐
     */
    public static final String music = "music";

    public static final String modelName = "cn.phenix.model.app.cms.CmsChannel";

}
