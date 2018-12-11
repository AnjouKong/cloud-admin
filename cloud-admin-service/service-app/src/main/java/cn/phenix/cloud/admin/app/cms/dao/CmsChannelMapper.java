package cn.phenix.cloud.admin.app.cms.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.cms.CmsChannel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CmsChannelMapper extends GenericJpaRepository<CmsChannel, String> {

    List<CmsChannel> findByParentIdAndDisabledAndType(String s, Sort sort,Boolean disabled,String type);

    @Modifying
    @Query(value="update CmsChannel set disabled = :disable where id = :menuId")
    void updateById(@Param("menuId") String menuId,@Param("disable") boolean disable);

    @Modifying
    @Query(value="update CmsChannel set location = :location where id = :id")
    void updateLocationById(@Param("location")int location, @Param("id")String id);

    @Modifying
    @Query(value="update CmsChannel set location = 0")
    void updateAllLocation();

    @Modifying
    @Query(value="update CmsChannel set hasChildren = :b where id= :pid")
    void updateHasChildren(@Param("b") boolean b, @Param("pid") String pid);

    int countByParentId(String parentId);

    @Modifying
    @Query(value="delete from CmsChannel where path like :id")
    void deletePathLikeId(@Param("id")String id);
}
