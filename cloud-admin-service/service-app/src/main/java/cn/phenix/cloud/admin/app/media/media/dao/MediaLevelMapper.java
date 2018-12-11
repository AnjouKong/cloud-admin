package cn.phenix.cloud.admin.app.media.media.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.media.MediaLevel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by fxq on 2017/10/17.
 */
public interface MediaLevelMapper extends GenericJpaRepository<MediaLevel,String> {

    List<MediaLevel> findByDelFlagOrderByCreateDate(String delFlag);

    @Modifying
    @Query("update MediaLevel set status=:status where id=:id ")
    void updateStatus(@Param("id")String id,@Param("status") String status);
}

