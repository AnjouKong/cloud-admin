package cn.phenix.model.app.cibn;

import cn.phenix.cloud.core.model.BaseModel;
import cn.phenix.cloud.core.utils.DateUtils;
import cn.phenix.model.app.media.MediaTag;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 节目集
 */
@Getter
@Setter
@Entity
@Table(name="APP_CIBN_SERIES")
public class CIBNMediaSeries extends BaseModel {

    private static final long serialVersionUID = 7271625495493724499L;

    //第三方节目集标识(方便资源核对)
    @Column
    private String seriesCode;
    //节目集名称
    @Column
    private String seriesName;
    //海报地址
    @Column(length = 500)
    private String pictureUrl;
    //内容类型
    @Column
    private String cpName;
    //节目集分类Id
    @Column
    private String categoryId;
    //节目集标签
    //@JoinTable(name= "APP_MEDIA_SERIES_TAG", joinColumns = {@JoinColumn(name = "media_series_id", nullable = false, updatable = true) },inverseJoinColumns = { @JoinColumn(name = "tag_id", nullable =false, updatable = true) })
    @Transient
    private List<MediaTag> mediaTags;
    //影片产地
    @Column
    private String originalCountry;
    //主演
    @Column
    private String actorDisplay;
    //导演
    @Column
    private String writerDisplay;
    //时长（分钟）
    @Column
    private Integer totalLength;
    //影片描述
    @Column(length = 2000)
    private String seriesDesc;
    //语言
    @Column
    private String seriesLanguage;
    //关键字
    @Column
    private String seriesKeyword;
    //付费标识(1:付费；2：免费)
    @Column
    private String cornerPrice;
    //影片年份
    @Column
    private String releaseYear;
    //节目集数
    @Column
    private Integer volumnCount;

    @Transient
    public String getUpdateDateStr(){
        return DateUtils.format(this.getUpdateDate(),"yyyy-MM-dd HH:mm:ss");
    }
}
