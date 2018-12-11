package cn.phenix.cloud.admin.tenant.renter.vo;

import cn.phenix.model.ui.v1.launcher.AppUI;
import lombok.Getter;
import lombok.Setter;

/**
 * mongodb存储实体
 **/
@Getter
@Setter
public class TenantSceneVo {

    private String id;
    private AppUI appUI;
    private Integer hashcode;
}
