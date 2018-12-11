package cn.phenix.model.app.language;

import cn.phenix.cloud.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


/**
 * 字段管理 fxq
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "APP_LANGUAGE_FIELD_MANAGEMENT")
public class FieldManagement extends BaseModel {

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private ModelManagement modelManagement;

    /**
     * 宾客名称
     */
    public static final String guestName="guestName";
}
