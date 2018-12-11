package cn.phenix.cloud.admin.app.media.tag.service;

import cn.phenix.cloud.admin.app.media.tag.dao.MediaTagMapper;
import cn.phenix.cloud.admin.app.vo.media.MediaSeriesTagVo;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.media.MediaTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.phenix.cloud.admin.app.media.tag.dao.MediaTagMapper.findSecondMenus;
import static cn.phenix.cloud.admin.app.media.tag.dao.MediaTagMapper.getSecondMenu;


/**
 * 标签管理
 **/
@Service
@Transactional(readOnly = true)
public class MediaTagService extends CoreService<MediaTagMapper, MediaTag> {


    public PageTable<MediaTag> findPage(String tagName, String levelId, DataTableParameter parameter) {
        PageRequest pageRequest = parameter.getPageRequest();
        Parameter p = Parameter.create(dao.findMediaTagInfo);
        if (!Strings.isBlank(tagName)) {
            p.insert("tagName", "%" + tagName + "%");
        }
        if (!Strings.isBlank(levelId)) {
            p.insert("levelId", levelId);
        }
        Page<MediaTag> page = dao.findPageByNamedQuery(p, pageRequest, MediaTag.class);
        return new PageTable<>(page);
    }


    public Integer findByTagName(String tagName, String id, String levelId) {
        Finder finder = Finder.create("from  MediaTag m where m.delFlag=:delFlag and m.tagName=:tagName ")
                .setParam("delFlag", MediaTag.DEL_FLAG_NORMAL).setParam("tagName", tagName);
        finder.search("id", id, Finder.SearchType.NEQ);
        if (Strings.isBlank(levelId)) {
            finder.append(" and (m.levelId is null or m.levelId ='') ");
        } else {
            finder.search("levelId", levelId);
        }
        List<MediaTag> mediaTagList = dao.find(finder);

        if (mediaTagList != null)
            return mediaTagList.size();

        return 0;
    }


    public List<MediaTag> findByDelFlagAndLevelIdAndType(String levelId, String type) {
        Finder finder = Finder.create(" from MediaTag ");
        finder.whereExcludeDel();
        finder.search("type", type);
        finder.append(" and levelId=:levelId ").setParam("levelId", levelId);

        List<MediaTag> list = dao.find(finder);
        return list;
    }

    public List<MediaTag> findByDelFlag(String delFlag) {
        return dao.findByDelFlag(delFlag);
    }

    public List<MediaTag> findFirstMenus(String levelId,String type) {
        Finder finder = Finder.create("from MediaTag ");
        finder.whereExcludeDel();
        finder.search("publish", true);
        finder.search("type", type);
        finder.append(" and levelId=:levelId ").setParam("levelId", levelId);
        finder.append(" order by index asc");
        List<MediaTag> firstMenus = dao.find(finder);
        return firstMenus;
    }

    public List<MediaSeriesTagVo> findSecondMenus(String levelId) {
        List<MediaSeriesTagVo> secondMenus = dao.findByNamedQuery(findSecondMenus, Parameter.create().insert("levelId", levelId), MediaSeriesTagVo.class);
        return secondMenus;
    }

    public List<MediaSeriesTagVo> getSecondMenu(String levelId, String tagId) {
        List<MediaSeriesTagVo> secondMenus = dao.findByNamedQuery(getSecondMenu, Parameter.create().insert("levelId", levelId).insert("tagId", tagId), MediaSeriesTagVo.class);
        return secondMenus;
    }

    public List<MediaSeriesTagVo> getSecondSubjectMenu(String levelId, String tagId) {
        List<MediaSeriesTagVo> secondMenus = dao.findByNamedQuery(dao.getSecondSubjectMenu, Parameter.create().insert("levelId", levelId).insert("tagId", tagId), MediaSeriesTagVo.class);
        return secondMenus;
    }

    @Transactional
    public void updateAllLocation(String levelId) {
        dao.updateByNamedQuery(Parameter.create(dao.updateAllLocation).insert("levelId", levelId));
    }

    @Transactional
    public void updateAllIndex(String levelId) {
        dao.updateAllIndex(levelId);
    }

    @Transactional
    public void updateLocation(int i, String seriesId, String tagId) {
        dao.updateByNamedQuery(Parameter.create(dao.updateLocation)
                .insert("location", i).insert("seriesId", seriesId).insert("tagId", tagId));
    }

    @Transactional
    public void updateSubjectLocation(int i, String subjectId, String tagId) {
        dao.updateByNamedQuery(Parameter.create(dao.updateSubjectLocation)
                .insert("location", i).insert("subjectId", subjectId).insert("tagId", tagId));
    }

}
