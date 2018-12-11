package cn.phenix.model.app.language;

import cn.phenix.cloud.core.model.BaseModel;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;


/**
 * 应用管理 fxq
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "APP_LANGUAGE_APPLICATION_MANAGEMENT")
public class ApplicationManagement extends BaseModel {

    /**
     * 名字
     */
    @Column(length = 50)
    private String name;

    /**
     * code
     */
    @Column(length = 50)
    private String code;

    /**
     * 模块
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "applicationId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<ModelManagement> modelManagementList = Lists.newArrayList();

}
