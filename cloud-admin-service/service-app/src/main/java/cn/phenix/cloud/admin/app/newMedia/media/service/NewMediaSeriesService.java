package cn.phenix.cloud.admin.app.newMedia.media.service;

import cn.phenix.cloud.admin.app.newMedia.media.dao.NewMediaMapper;
import cn.phenix.cloud.admin.app.newMedia.media.dao.NewMediaSeriesFuncMapper;
import cn.phenix.cloud.admin.app.newMedia.media.dao.NewMediaSeriesMapper;
import cn.phenix.cloud.admin.app.newMedia.tag.dao.NewMediaTagMapper;
import cn.phenix.cloud.admin.app.vo.media.NewMediaSeriesTagVo;
import cn.phenix.cloud.admin.sys.service.SysDictService;
import cn.phenix.cloud.core.utils.MyBeanUtils;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.newMedia.NewMediaSeries;
import cn.phenix.model.app.newMedia.NewMediaSeriesFunc;
import cn.phenix.model.app.newMedia.NewMediaTag;
import cn.phenix.model.sys.SysDict;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class NewMediaSeriesService extends CoreService<NewMediaSeriesMapper, NewMediaSeries> {

    @Autowired
    private NewMediaTagMapper newMediaTagMapper;
    @Autowired
    private NewMediaMapper newMediaMapper;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private NewMediaSeriesFuncMapper newMediaSeriesFuncMapper;


    public PageTable<NewMediaSeries> findPage(String sreleaseYear, String ereleaseYear, String screateTime,
                                              String ecreateTime, String scharge, String echarge, NewMediaSeries mediaSeries, DataTableParameter parameter) throws ParseException {
        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("from NewMediaSeries where delFlag='0'");


        if (!Strings.isBlank(mediaSeries.getCategoryId())) {
            finder.search("categoryId", Arrays.asList(mediaSeries.getCategoryId().split(",")), Finder.SearchType.IN);
        }
        if (!Strings.isBlank(mediaSeries.getPublish())) {
            String[] publishs = mediaSeries.getPublish().split(",");
            finder.search("publish", Arrays.asList(publishs), Finder.SearchType.IN);
        }
        finder.search("releaseYear", sreleaseYear, Finder.SearchType.GE);
        finder.search("releaseYear", ereleaseYear, Finder.SearchType.LE);
        if (StringUtils.isNotBlank(screateTime)) {
            SimpleDateFormat startDate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            Date date = startDate.parse(screateTime + " 00:00:00");
            //2018年1月2日16:37:09 统一为更新时间
            finder.search("updateDate", date, Finder.SearchType.GE);
        }
        if (StringUtils.isNotBlank(ecreateTime)) {
            SimpleDateFormat endDate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            Date date = endDate.parse(ecreateTime + " 23:59:59");
            //2018年1月2日16:37:09 统一为更新时间
            finder.search("updateDate", date, Finder.SearchType.LE);
        }
        if (StringUtils.isNotBlank(scharge)) {
            finder.search("charge", Integer.parseInt(scharge), Finder.SearchType.GE);
        }
        if (StringUtils.isNotBlank(echarge)) {
            finder.search("charge", Integer.parseInt(echarge), Finder.SearchType.LE);
        }

        finder.search("seriesName", mediaSeries.getSeriesName(), Finder.SearchType.LIKE);
        finder.search("cpName", mediaSeries.getCpName(), Finder.SearchType.LIKE);
        finder.search("originalCountry", mediaSeries.getOriginalCountry(), Finder.SearchType.LIKE);
        finder.search("seriesKeyword", mediaSeries.getSeriesKeyword(), Finder.SearchType.LIKE);
        finder.append("order by updateDate desc");

        Page<NewMediaSeries> page = dao.find(finder, pageRequest);

        return new PageTable<>(page);
    }

    public PageTable<NewMediaSeries> findPublishedPage(String sreleaseYear, String ereleaseYear, String screateTime,
                                                       String ecreateTime, String scharge, String echarge, NewMediaSeries mediaSeries, DataTableParameter parameter) throws ParseException {
        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("from NewMediaSeries where delFlag='0'");//(levelId is null or levelId = '')

        if (!Strings.isBlank(mediaSeries.getCategoryId())) {
            finder.search("categoryId", Arrays.asList(mediaSeries.getCategoryId().split(",")), Finder.SearchType.IN);
        }
        if (!Strings.isBlank(mediaSeries.getPublish())) {
            String[] publishs = mediaSeries.getPublish().split(",");
            finder.search("publish", Arrays.asList(publishs), Finder.SearchType.IN);
        }
        finder.search("releaseYear", sreleaseYear, Finder.SearchType.GE);
        finder.search("releaseYear", ereleaseYear, Finder.SearchType.LE);
        if (StringUtils.isNotBlank(screateTime)) {
            SimpleDateFormat startDate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            Date date = startDate.parse(screateTime + " 00:00:00");
            //2018年1月2日16:37:09 统一为更新时间
            finder.search("updateDate", date, Finder.SearchType.GE);
        }
        if (StringUtils.isNotBlank(ecreateTime)) {
            SimpleDateFormat endDate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            Date date = endDate.parse(ecreateTime + " 23:59:59");
            //2018年1月2日16:37:09 统一为更新时间
            finder.search("updateDate", date, Finder.SearchType.LE);
        }
        if (StringUtils.isNotBlank(scharge)) {
            finder.search("charge", Integer.parseInt(scharge), Finder.SearchType.GE);
        }
        if (StringUtils.isNotBlank(echarge)) {
            finder.search("charge", Integer.parseInt(echarge), Finder.SearchType.LE);
        }

        finder.search("seriesName", mediaSeries.getSeriesName(), Finder.SearchType.LIKE);
        finder.search("cpName", mediaSeries.getCpName(), Finder.SearchType.LIKE);
        finder.search("originalCountry", mediaSeries.getOriginalCountry(), Finder.SearchType.LIKE);
        finder.search("seriesKeyword", mediaSeries.getSeriesKeyword(), Finder.SearchType.LIKE);
        finder.search("publish", "1");
        finder.append("order by updateDate desc");

        Page<NewMediaSeries> page = dao.find(finder, pageRequest);

        return new PageTable<>(page);
    }

    @Transactional
    public void editMediaSeries(NewMediaSeries mediaSeries, String[] mediasTags) {
        NewMediaSeries mediaSeries2 = get(mediaSeries.getId());
        MyBeanUtils.to(mediaSeries, mediaSeries2);

        //设置标签
        mediaSeries2.getMediaTagsList().clear();
        if (mediasTags != null && mediasTags.length > 0) {
            for (int i = 0; i < mediasTags.length; i++) {
                mediaSeries2.getMediaTagsList().add(new NewMediaTag(mediasTags[i].split("_")[0]));
            }
        }

        saveOrUpdate(mediaSeries2);

        //媒资和功能（4k、杜比、蓝光灯）关联2017年12月18日14:49:54
        newMediaSeriesFuncMapper.deleteBySeriesId(mediaSeries.getId());
        if (StringUtils.isNoneBlank(mediaSeries.getFuncIds())) {
            String[] funcIds = mediaSeries.getFuncIds().split(",");
            for (int i = 0; i < funcIds.length; i++) {
                NewMediaSeriesFunc brandSpecificationSeries = new NewMediaSeriesFunc();
                SysDict sysDict = sysDictService.get(funcIds[i]);
                brandSpecificationSeries.setSeriesId(mediaSeries.getId());
                brandSpecificationSeries.setFuncId(funcIds[i]);
                brandSpecificationSeries.setFuncName(sysDict.getName());
                brandSpecificationSeries.setParentId(sysDict.getParentId());
                newMediaSeriesFuncMapper.save(brandSpecificationSeries);
            }
        }
    }

    @Transactional
    public void offLine(String id) {
        dao.offLine(id);
    }

    @Transactional
    public void release(String id) {
        dao.release(id);
    }

    @Transactional
    public void saveList(List<NewMediaSeries> mediaSeriesList) {
        dao.saveAll(mediaSeriesList);
    }

    @Transactional
    public void updateLocation(List<NewMediaSeriesTagVo> tagNowList) {
        if (tagNowList != null && tagNowList.size() > 0) {
            for (NewMediaSeriesTagVo vo : tagNowList) {
                newMediaTagMapper.updateByNamedQuery(Parameter.create(newMediaTagMapper.updateLocation).insert("seriesId", vo.getMediaSeriesId()).insert("tagId", vo.getTagId()).insert("location", vo.getLocation()));
            }
        }
    }

    @Transactional
    public void addMediaSeriesTag(String seriesId, String tagId) {
        newMediaTagMapper.updateByNamedQuery(Parameter.create(newMediaTagMapper.addMediaSeriesTag).insert("seriesId", seriesId).insert("tagId", tagId));
    }


    public List<NewMediaSeriesTagVo> tagNowList(String seriesId) {
        Parameter parameter = Parameter.create().insert("seriesId", seriesId);
        List<NewMediaSeriesTagVo> findTagNowList = newMediaTagMapper.findByNamedQuery(newMediaTagMapper.tagNowList, parameter, NewMediaSeriesTagVo.class);
        return findTagNowList;
    }

    @Transactional
    public void delMedias(String[] ids) {
        for (String id : ids) {
            dao.delMedias(id);
            //删除resource
            newMediaMapper.deleteBySeriesId(id);
            //媒资-功能关联表删除
            newMediaSeriesFuncMapper.deleteBySeriesId(id);
        }
    }

    public List<NewMediaTag> findByDelFlagAndTagNameAndLevelId(String s, String tagName) {
        return newMediaTagMapper.findByDelFlagAndTagName(s, tagName);
    }

    @Transactional
    public void updateIsRecommend(String mediaId, String isRecommend) {
        dao.updateIsRecommend(mediaId, isRecommend);
    }

    @Transactional
    public void updateIsHotWord(String mediaId, String isHotWord) {
        dao.updateIsHotWord(mediaId, isHotWord);
    }
}
