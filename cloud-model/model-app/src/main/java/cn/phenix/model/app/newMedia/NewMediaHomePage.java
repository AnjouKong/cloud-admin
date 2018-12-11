package cn.phenix.model.app.newMedia;

import cn.phenix.cloud.core.model.BaseModel;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import java.util.List;


/**
 * 媒资首页
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "MEDIA_HOMEPAGE")
public class NewMediaHomePage extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String modelName = "cn.phenix.model.app.newMedia.NewMediaHomePage";
    /**
     * 首页名称
     */
    @Column(length = 100)
    private String homePageName;

    /**
     * 首页顶部
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "homePageId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @OrderBy(clause = "location asc")
    private List<NewMediaHomePageTop> topList = Lists.newArrayList();


    /**
     * 媒资列表
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "homePageId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @OrderBy(clause = "location asc")
    private List<NewMediaHomePageDetail> mediaList = Lists.newArrayList();

    @Transient
    private String selectedHomePageTopArr;
    @Transient
    private String mediaArr;
}
