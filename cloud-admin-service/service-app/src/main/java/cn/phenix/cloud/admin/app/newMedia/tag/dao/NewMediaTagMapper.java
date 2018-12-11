package cn.phenix.cloud.admin.app.newMedia.tag.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.newMedia.NewMediaTag;

import java.util.List;

/**
 * 标签管理
 **/
public interface NewMediaTagMapper extends GenericJpaRepository<NewMediaTag, String> {

    String findSecondMenus = "findSecondMenus_new";

    String getSecondMenu = "getSecondMenu_new";
    String getSecondSubjectMenu = "getSecondSubjectMenu_new";

    String updateAllLocation = "updateAllLocation_new";

    String updateLocation = "updateLocation_new";
    String updateSubjectLocation = "updateSubjectLocation_new";

    String tagNowList = "tagNowList_new";

    String delTagSeries = "delTagSeries_new";

    String addMediaSeriesTag = "addMediaSeriesTag_new";
    String seriesVoSearch = "seriesVoSearch_new";
    String findSeriesTagVo = "findSeriesTagVo_new";
    String insertData = "insertData_new";
    String findMediaTagInfo = "findMediaTagInfo_new";


    List<NewMediaTag> findByDelFlag(String delFlag);

    List<NewMediaTag> findByDelFlagAndTagName(String s, String tagName);
}
