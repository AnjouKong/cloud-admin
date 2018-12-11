package cn.phenix.cloud.admin.app.media.tag.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.media.MediaTag;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 标签管理
 **/
public interface MediaTagMapper extends GenericJpaRepository<MediaTag, String> {

    String findSecondMenus = "findSecondMenus";

    String getSecondMenu = "getSecondMenu";
    String getSecondSubjectMenu = "getSecondSubjectMenu";

    String updateAllLocation = "updateAllLocation";

    String updateLocation = "updateLocation";
    String updateSubjectLocation = "updateSubjectLocation";

    String tagNowList = "tagNowList";

    String delTagSeries = "delTagSeries";

    String addMediaSeriesTag = "addMediaSeriesTag";
    String seriesVoSearch = "seriesVoSearch";
    String findSeriesTagVo = "findSeriesTagVo";
    String insertData = "insertData";
    String findMediaTagInfo = "findMediaTagInfo";


    List<MediaTag> findByDelFlagAndLevelId(String delFlag, String levelId);

    List<MediaTag> findByDelFlag(String delFlag);

    @Modifying
    @Query("update MediaTag set index=0 where levelId=:levelId")
    void updateAllIndex(@Param("levelId") String levelId);

    void deleteByLevelId(String id);

    List<MediaTag> findByDelFlagAndTagNameAndLevelId(String s, String tagName, String levelId);
}
