package cn.phenix.model.app.newMedia;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 媒资聚合规则
 *
 **/

@Getter
@Setter
@Entity
@Table(name="MEDIA_CONVERGE_RULE")
@DynamicUpdate
@DynamicInsert
public class NewMediaConvergeRule extends BaseModel {
    //规则名称
    @Column
    private String name;

    //规则标识：字典表的id
    @Column
    private String code;


}
