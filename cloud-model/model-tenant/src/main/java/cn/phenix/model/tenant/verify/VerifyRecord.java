package cn.phenix.model.tenant.verify;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 审核记录
 * Created by mgm
 */
@Getter
@Setter
@Entity
@Table(name="APP_VERIFY_RECORD")
public class VerifyRecord extends BaseModel {

    private static final long serialVersionUID = 6015786810399165687L;

    //审核原因
    @Column
    private String reason;

    //series表id
    @Column
    private String seriesId;
}
