package cn.phenix.model.app.media;

import cn.phenix.cloud.core.model.BaseModel;
import cn.phenix.cloud.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 节目集
 * Created by xiaobin on 2017/8/14.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "APP_MEDIA_SERIES")
public class MediaSeries extends BaseModel {

    private static final long serialVersionUID = 3363433244148852825L;
    //第三方节目集标识(方便资源核对)
    @Column
    private String seriesCode;
    //节目集名称
    @Column
    private String seriesName;
    //海报地址
    @Column(length = 500)
//    @ColDefine(width = 500)
    private String pictureUrl;
    //内容类型
    @Column
    private String cpName;
    //节目集分类Id
    @Column
    private String categoryId;
    //节目集标签
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "APP_MEDIA_SERIES_TAG", joinColumns = {@JoinColumn(name = "media_series_id", nullable = false, updatable = true)}
            , inverseJoinColumns = {@JoinColumn(name = "tag_id", nullable = false, updatable = true)})
    @JsonIgnore
    private List<MediaTag> mediaTagsList = Lists.newArrayList();
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
//    @ColDefine(width = 2000)
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
    //费用
    @Column(precision = 2)
//    @ColDefine(precision = 2)
    private BigDecimal charge;
    //原价
    @Column(precision = 2)
    private BigDecimal originalPrice;

    //来源
    @Column(length = 50)
//    @ColDefine(width = 50)
    private String source;

    //评分
    @Column
    private Double score;

    //是否热门（1：是，其他否）
    @Column
    private Integer hot;

    /**
     * 是否启用
     */
    @Column
    private String publish;
    /**
     * 审核状态
     * 0.初始
     * 1.通过
     * 2.拒绝
     * 3.待审核
     */
    @Column(name = "status_")
//    @TableField("status_")
    private String status;

    /**
     * 继承Id
     */
    @Column
    private String extendId;
    /**
     * 级别Id
     */
    @Column
    private String levelId;
    /**
     * 是否沿用继承媒资信息 0 否 1 是
     */
    @Column
    private String follow;

    @Transient
    public String getUpdateDateStr() {
        return DateUtils.format(this.getUpdateDate(), "yyyy-MM-dd HH:mm:ss");
    }

    @Transient
    public BigDecimal getShowCharge() {
        return this.getCharge();
    }

    /**
     * 功能ids，如蓝光，杜比，普通等
     */
    @Column
    private String funcIds;

    /**
     * 是否推荐0否1是
     */
    @Column
    private String isRecommend;

    /**
     * 是否热搜0否1是
     */
    @Column
    private String isHotWord;
}
