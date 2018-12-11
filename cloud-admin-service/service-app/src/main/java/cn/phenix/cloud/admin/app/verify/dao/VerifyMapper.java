package cn.phenix.cloud.admin.app.verify.dao;//package cn.phenix.cloud.admin.tenant.verify.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.media.MediaSeries;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**

import MediaSeries;* Created by mgm
 */
public interface VerifyMapper extends GenericJpaRepository<MediaSeries,String> {


    @Modifying
    @Query("update MediaSeries set status_=:status,publish=:publish where id =:id")
    void updateMediaseries(@Param("status")String status, @Param("id")String str, @Param("publish")String publish);
}

