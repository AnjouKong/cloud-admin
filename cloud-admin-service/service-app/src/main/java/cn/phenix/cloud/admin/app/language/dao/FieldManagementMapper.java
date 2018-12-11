package cn.phenix.cloud.admin.app.language.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.language.FieldManagement;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**

* Created by mgm
 */
public interface FieldManagementMapper extends GenericJpaRepository<FieldManagement,String> {

    @Modifying
    @Query("update FieldManagement set delFlag='1' where modelManagement.id in :ids")
    void updateDelFlagByModelIdIn(@Param("ids")List<String> ids);

    @Modifying
    @Query("delete from FieldManagement where modelManagement.id = :modelId")
    void deleteByModelId(@Param("modelId")String modelId);
}

