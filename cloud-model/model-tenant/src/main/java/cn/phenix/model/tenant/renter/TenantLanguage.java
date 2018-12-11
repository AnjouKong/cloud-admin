package cn.phenix.model.tenant.renter;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 商户多语言关联
 */

@Getter
@Setter
@Entity
@Table(name = "t_tenant_language")
public class TenantLanguage extends TenantBaseModel {

    /**
     * 语言code
     */
    private String languageDicCode;

    @Transient
    private String[] languageDic;
    @Transient
    private String languageName;

}
