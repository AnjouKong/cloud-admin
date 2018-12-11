package cn.phenix.model.tenant.channel;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 电视直播
 * Created by fxq
 */
@Getter
@Setter
@Entity
@Table(name="APP_CHANNEL")
public class Channel extends TenantBaseModel implements Serializable {


    @Column(length = 50)
    private String name;

    /**
     * 频道号
     */
    @Column(length = 5)
    private Integer number;

    @Column(length = 100)
    private String url;

}
