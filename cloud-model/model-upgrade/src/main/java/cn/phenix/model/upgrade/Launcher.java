package cn.phenix.model.upgrade;

import cn.phenix.cloud.core.model.BaseModel;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * @author xiaobin
 * @create 2018-01-02 下午6:36
 **/
@Getter
@Setter
@Table(name = "t_upgrade_launcher")
@Entity
@DynamicUpdate
@DynamicInsert
public class Launcher extends BaseModel {

    //名称
    private String name;
    //标识
    private String code;
    //包名
    private String packageName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "launcherId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<LauncherVersion> childDeptList= Lists.newArrayList();

}
