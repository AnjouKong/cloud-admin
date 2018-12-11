package cn.phenix.cloud.admin.app.language.dao;//package cn.phenix.cloud.admin.tenant.verify.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.language.ApplicationManagement;
import cn.phenix.model.app.language.ModelManagement;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**

* Created by mgm
 */
public interface ModelManagementMapper extends GenericJpaRepository<ModelManagement,String> {

    @Modifying
    @Query("update ModelManagement set delFlag='1' where id in :ids")
    void updateDelFlagByIdIn(@Param("ids") String[] ids);
}

