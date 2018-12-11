package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created
 */
@Setter
@Getter
@Table(name = "sys_private_route")
@Entity
@DynamicInsert
@DynamicUpdate
public class SysPrivateRoute extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    private String routeName;

    /**
     * 匹配信息
     */
    @ElementCollection(fetch= FetchType.LAZY, //加载策略,延迟加载
            targetClass=String.class) //指定集合中元素的类型
    @CollectionTable(name="sys_private_route_map") //指定集合生成的表
    @MapKeyColumn(name="marryRule") //指定map的key生成的列
    private Map<String,String> marryUrl=new HashMap<>();


    @Transient
    public int getMarrySize(){return this.getMarryUrl().size();}

}
