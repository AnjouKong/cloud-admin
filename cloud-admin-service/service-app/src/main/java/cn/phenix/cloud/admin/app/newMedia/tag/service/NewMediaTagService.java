package cn.phenix.cloud.admin.app.newMedia.tag.service;

import cn.phenix.cloud.admin.app.newMedia.tag.dao.NewMediaTagMapper;
import cn.phenix.cloud.admin.app.vo.media.NewMediaSeriesTagVo;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.media.MediaTag;
import cn.phenix.model.app.newMedia.NewMediaTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 标签管理
 **/
@Service
@Transactional(readOnly = true)
public class NewMediaTagService extends CoreService<NewMediaTagMapper, NewMediaTag> {


    public PageTable<NewMediaTag> findPage(String tagName, DataTableParameter parameter) {
        PageRequest pageRequest = parameter.getPageRequest();
        Parameter p = Parameter.create(dao.findMediaTagInfo);
        if (!Strings.isBlank(tagName)) {
            p.insert("tagName", "%" + tagName + "%");
        }
        Page<NewMediaTag> page = dao.findPageByNamedQuery(p, pageRequest, NewMediaTag.class);
        return new PageTable<>(page);
    }


    public Integer findByTagName(String tagName, String id) {
        Finder finder = Finder.create("from NewMediaTag m where m.delFlag=:delFlag and m.tagName=:tagName ")
                .setParam("delFlag", MediaTag.DEL_FLAG_NORMAL).setParam("tagName", tagName);
        finder.search("id", id, Finder.SearchType.NEQ);

        List<NewMediaTag> mediaTagList = dao.find(finder);
        if (mediaTagList != null)
            return mediaTagList.size();

        return 0;
    }


    public List<NewMediaTag> findByDelFlagAndType(String type) {
        Finder finder = Finder.create(" from NewMediaTag ");
        finder.whereExcludeDel();
        finder.search("type", type);
        List<NewMediaTag> list = dao.find(finder);
        return list;
    }

    public List<NewMediaTag> findByDelFlag(String delFlag) {
        return dao.findByDelFlag(delFlag);
    }

    public List<NewMediaTag> findFirstMenus(String type) {
        Finder finder = Finder.create("from NewMediaTag ");
        finder.whereExcludeDel();
        finder.search("publish", true);
        finder.search("type", type);
        finder.append(" order by index asc");
        List<NewMediaTag> firstMenus = dao.find(finder);
        return firstMenus;
    }

    public List<NewMediaSeriesTagVo> findSecondMenus() {
        List<NewMediaSeriesTagVo> secondMenus = dao.findByNamedQuery(dao.findSecondMenus, null, NewMediaSeriesTagVo.class);
        return secondMenus;
    }

    public List<NewMediaSeriesTagVo> getSecondMenu(String tagId) {
        List<NewMediaSeriesTagVo> secondMenus = dao.findByNamedQuery(dao.getSecondMenu, Parameter.create().insert("tagId", tagId), NewMediaSeriesTagVo.class);
        return secondMenus;
    }

    public List<NewMediaSeriesTagVo> getSecondSubjectMenu(String tagId) {
        List<NewMediaSeriesTagVo> secondMenus = dao.findByNamedQuery(dao.getSecondSubjectMenu, Parameter.create().insert("tagId", tagId), NewMediaSeriesTagVo.class);
        return secondMenus;
    }

    @Transactional
    public void updateAllLocation(String levelId) {
        dao.updateByNamedQuery(Parameter.create(dao.updateAllLocation).insert("levelId", levelId));
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
