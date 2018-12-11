package cn.phenix.model.app.cms;


import cn.phenix.cloud.core.model.BaseModel;
import cn.phenix.model.app.language.LanguageDic;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Getter
@Setter
@Entity
@Table(name = "cms_resources_language")
public class CmsResourcesLanguage extends BaseModel {

    /**
     * 资源类型 图片、图片集合、视频、文章、APP
     */
    private String resourcesType;

    /**
     * 资源id
     */
    private String resourcesId;

    /**
     * 语言code
     */
    private String language;
    /**
     * 语言名称
     */
    @Transient
    private String languageName;


    @Transient
    private List<String> resourcesUrlList= Lists.newArrayList();

    @Transient
    private List<Map<String,String>> resourcesUrlMap= Lists.newArrayList();

    @Transient
    private String resourcesUrl;

    @Transient
    private LanguageDic languageDic;

    @Transient
    private String attId;

    @Transient
    private int num;




    /**
     * 类型  图片
     */
    public static final String image = "image";
    /**
     * 类型  图片集合
     */
    public static final String imageList = "imageList";
    /**
     * 类型  APP
     */
    public static  final String App="App";

    /**
     * 类型  html
     */
    public static  final String html="html";
    /**
     * 类型  视频
     */
    public static  final String video="video";

    /**
     * 类型  广告
     */
    public static  final String adv="adv";

    /**
     * 类型  语言
     */
    public static  final String language_="language";
    /**
     * 类型  pms
     */
    public static  final String pms="pms";
    /**
     * 类型  音乐
     */
    public static  final String music="music";
    /**
     * 类型 主题包
     */
    public static final String theme="theme";
    /**
     * 自定义页面
     */
    public static final String custom="custom";
    /**
     * 二级页面
     */
    public static final String secondary="secondary";


}
