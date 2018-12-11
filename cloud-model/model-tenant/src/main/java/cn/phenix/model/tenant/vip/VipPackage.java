package cn.phenix.model.tenant.vip;

import cn.phenix.cloud.core.model.BaseModel;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * @author socilents
 * Create in 2018/5/24 11:27
 **/
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="APP_PACKAGE_MAIN")
public class VipPackage extends BaseModel {
    public static final String modelName = "cn.phenix.model.tenant.vip.VipPackage";
    public static final String fdKey = "backgroundPic";
    private String packageName;//套餐包名
    private String backgroundPic;//套餐包背景图，url
    @Column(length = 2000)
    private String note;//温馨提示

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "packageId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<VipContent> contentList = Lists.newArrayList();
}
