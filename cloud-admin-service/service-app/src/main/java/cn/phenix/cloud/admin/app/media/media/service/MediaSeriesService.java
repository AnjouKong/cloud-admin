package cn.phenix.cloud.admin.app.media.media.service;

import cn.phenix.cloud.admin.app.media.media.dao.MediaMapper;
import cn.phenix.cloud.admin.app.media.media.dao.MediaSeriesFuncMapper;
import cn.phenix.cloud.admin.app.media.media.dao.MediaSeriesMapper;
import cn.phenix.cloud.admin.app.media.tag.dao.MediaTagMapper;
import cn.phenix.cloud.admin.app.vo.media.MediaSeriesTagVo;
import cn.phenix.cloud.admin.sys.service.SysDictService;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.MyBeanUtils;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.media.MediaSeries;
import cn.phenix.model.app.media.MediaSeriesFunc;
import cn.phenix.model.app.media.MediaTag;
import cn.phenix.model.sys.SysDict;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.phenix.cloud.admin.app.media.tag.dao.MediaTagMapper.tagNowList;

/**
 * Created by 马刚铭 on 2017/8/16.
 */
@Service
@Transactional(readOnly = true)
public class MediaSeriesService extends CoreService<MediaSeriesMapper,MediaSeries>{
    @Autowired
    private MediaSeriesMapper mediaSeriesMapper;
    @Autowired
    private MediaTagMapper mediaTagMapper;
    @Autowired
    private MediaMapper mediaMapper;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private MediaSeriesFuncMapper mediaSeriesFuncMapper;


    public PageTable<MediaSeries> findPage(String sreleaseYear, String ereleaseYear,String screateTime,
                                           String ecreateTime,String scharge,String echarge,MediaSeries mediaSeries, DataTableParameter parameter) throws ParseException {
        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("from MediaSeries where  1=1 and delFlag='0'");//(levelId is null or levelId = '')

        finder.search("levelId", mediaSeries.getLevelId(),Finder.SearchType.EQ);

        if (!Strings.isBlank(mediaSeries.getCategoryId())) {
            finder.search("categoryId", Arrays.asList(mediaSeries.getCategoryId().split(",")),Finder.SearchType.IN);
        }
        if (!Strings.isBlank(mediaSeries.getPublish())) {
            String[] publishs = mediaSeries.getPublish().split(",");
            finder.search("publish",Arrays.asList(publishs),Finder.SearchType.IN);
        }
        if (!Strings.isBlank(mediaSeries.getStatus())) {
            String[] statuss = mediaSeries.getStatus().split(",");
            finder.search("status",Arrays.asList(statuss),Finder.SearchType.IN);
        }
        finder.search("releaseYear",sreleaseYear,Finder.SearchType.GE);
        finder.search("releaseYear",ereleaseYear,Finder.SearchType.LE);
        if(StringUtils.isNotBlank(screateTime)){
            SimpleDateFormat startDate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            java.util.Date date=startDate.parse(screateTime+" 00:00:00");
            //2018年1月2日16:37:09 统一为更新时间
            finder.search("updateDate",date,Finder.SearchType.GE);
        }
        if(StringUtils.isNotBlank(ecreateTime)){
            SimpleDateFormat endDate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            java.util.Date date=endDate.parse(ecreateTime+" 23:59:59");
            //2018年1月2日16:37:09 统一为更新时间
            finder.search("updateDate",date,Finder.SearchType.LE);
        }
        if(StringUtils.isNotBlank(scharge)){
            //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
            BigDecimal bd= new BigDecimal(scharge);
            bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            finder.search("charge",bd,Finder.SearchType.GE);
        }
        if(StringUtils.isNotBlank(echarge)){
            //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
            BigDecimal bd= new BigDecimal(echarge);
            bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            finder.search("charge",bd,Finder.SearchType.LE);
        }

        finder.search("seriesName",mediaSeries.getSeriesName(),Finder.SearchType.LIKE);
        finder.search("cpName",mediaSeries.getCpName(),Finder.SearchType.LIKE);
        finder.search("originalCountry",mediaSeries.getOriginalCountry(),Finder.SearchType.LIKE);
        finder.search("seriesKeyword",mediaSeries.getSeriesKeyword(),Finder.SearchType.LIKE);
        finder.append("order by updateDate desc");

        Page<MediaSeries> page = mediaSeriesMapper.find(finder,pageRequest);

        return new PageTable<>(page);
    }

    public PageTable<MediaSeries> findPublishedPage(String sreleaseYear, String ereleaseYear,String screateTime,
                                           String ecreateTime,String scharge,String echarge,MediaSeries mediaSeries, DataTableParameter parameter) throws ParseException {
        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("from MediaSeries where  1=1 and delFlag='0'");//(levelId is null or levelId = '')

        finder.search("levelId", mediaSeries.getLevelId(),Finder.SearchType.EQ);

        if (!Strings.isBlank(mediaSeries.getCategoryId())) {
            finder.search("categoryId", Arrays.asList(mediaSeries.getCategoryId().split(",")),Finder.SearchType.IN);
        }
        if (!Strings.isBlank(mediaSeries.getPublish())) {
            String[] publishs = mediaSeries.getPublish().split(",");
            finder.search("publish",Arrays.asList(publishs),Finder.SearchType.IN);
        }
        if (!Strings.isBlank(mediaSeries.getStatus())) {
            String[] statuss = mediaSeries.getStatus().split(",");
            finder.search("status",Arrays.asList(statuss),Finder.SearchType.IN);
        }
        finder.search("releaseYear",sreleaseYear,Finder.SearchType.GE);
        finder.search("releaseYear",ereleaseYear,Finder.SearchType.LE);
        if(StringUtils.isNotBlank(screateTime)){
            SimpleDateFormat startDate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            Date date=startDate.parse(screateTime+" 00:00:00");
            //2018年1月2日16:37:09 统一为更新时间
            finder.search("updateDate",date,Finder.SearchType.GE);
        }
        if(StringUtils.isNotBlank(ecreateTime)){
            SimpleDateFormat endDate = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            Date date=endDate.parse(ecreateTime+" 23:59:59");
            //2018年1月2日16:37:09 统一为更新时间
            finder.search("updateDate",date,Finder.SearchType.LE);
        }
        if(StringUtils.isNotBlank(scharge)){
            //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
            BigDecimal bd= new BigDecimal(scharge);
            bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            finder.search("charge",bd,Finder.SearchType.GE);
        }
        if(StringUtils.isNotBlank(echarge)){
            //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
            BigDecimal bd= new BigDecimal(echarge);
            bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            finder.search("charge",bd,Finder.SearchType.LE);
        }

        finder.search("seriesName",mediaSeries.getSeriesName(),Finder.SearchType.LIKE);
        finder.search("cpName",mediaSeries.getCpName(),Finder.SearchType.LIKE);
        finder.search("originalCountry",mediaSeries.getOriginalCountry(),Finder.SearchType.LIKE);
        finder.search("seriesKeyword",mediaSeries.getSeriesKeyword(),Finder.SearchType.LIKE);
        finder.search("publish","1");
        finder.append("order by updateDate desc");

        Page<MediaSeries> page = dao.find(finder,pageRequest);

        return new PageTable<>(page);
    }
    @Transactional
    public void editMediaSeries(MediaSeries mediaSeries, String[] mediasTags) {
        MediaSeries mediaSeries2 = get(mediaSeries.getId());
        MyBeanUtils.to(mediaSeries,mediaSeries2);
        mediaSeries2.setStatus("3");//审核状态  1.通过  2.拒绝  3.待审核
        if(mediaSeries.getCharge().compareTo(BigDecimal.ZERO) ==1 ){
            mediaSeries2.setCornerPrice("1");//收费标识
        }else {
            mediaSeries2.setCornerPrice("2");//免费
        }
        //设置标签
        mediaSeries2.getMediaTagsList().clear();
        if(mediasTags!= null && mediasTags.length >0){
            for(int i=0;i<mediasTags.length;i++){
                mediaSeries2.getMediaTagsList().add(new MediaTag(mediasTags[i].split("_")[0]));
            }
        }

        //修改沿用此信息的媒资
        if("1".equals(mediaSeries2.getLevelId())){
            saveOrUpdate(mediaSeries2);
            Finder finder=Finder.create("from MediaSeries ");
            finder.whereExcludeDel();
            finder.search("extendId",mediaSeries2.getId());
            finder.search("follow","1");
            List<MediaSeries> seriesList=dao.find(finder);
            for(MediaSeries m:seriesList){
                BeanUtils.copyProperties(mediaSeries2,m,"id","extendId","follow","keepMySelf","levelId","mediaTagsList");
                saveOrUpdate(m);
            }

        }else{  //若非标准媒资库，修改后与原继承标准媒资无关
            mediaSeries2.setFollow("0");
            saveOrUpdate(mediaSeries2);
        }

        //媒资和功能（4k、杜比、蓝光灯）关联2017年12月18日14:49:54
        mediaSeriesFuncMapper.deleteBySeriesId(mediaSeries.getId());
        if(StringUtils.isNoneBlank(mediaSeries.getFuncIds())){
            String[] funcIds = mediaSeries.getFuncIds().split(",");
            for(int i=0;i<funcIds.length;i++){
                MediaSeriesFunc brandSpecificationSeries = new MediaSeriesFunc();
                SysDict sysDict =sysDictService.get(funcIds[i]);
                brandSpecificationSeries.setSeriesId(mediaSeries.getId());
                brandSpecificationSeries.setFuncId(funcIds[i]);
                brandSpecificationSeries.setFuncName(sysDict.getName());
                brandSpecificationSeries.setParentId(sysDict.getParentId());
                mediaSeriesFuncMapper.save(brandSpecificationSeries);
            }
        }
    }

    @Transactional
    public void offLine(String id) {
        mediaSeriesMapper.offLine(id);
    }

    @Transactional
    public void saveList(List<MediaSeries> mediaSeriesList){
        dao.saveAll(mediaSeriesList);
    }

    @Transactional
    public void updateLocation (List<MediaSeriesTagVo> tagNowList){
        if(tagNowList !=null && tagNowList.size()>0){
            for(MediaSeriesTagVo vo:tagNowList){
                mediaTagMapper.updateByNamedQuery(Parameter.create(mediaTagMapper.updateLocation).insert("seriesId",vo.getMediaSeriesId()).insert("tagId",vo.getTagId()).insert("location",vo.getLocation()));
            }
        }
    }

    @Transactional
    public void addMediaSeriesTag (String seriesId,String tagId){
        mediaTagMapper.updateByNamedQuery(Parameter.create(mediaTagMapper.addMediaSeriesTag).insert("seriesId",seriesId).insert("tagId",tagId));
    }

    public  List<MediaSeries> findByExtendIdAndLevelId(String extendId,String levelId){
        Finder finder=Finder.create("from MediaSeries");
        finder.whereExcludeDel();
        finder.search("extendId",extendId);
        finder.search("levelId",levelId);
        List<MediaSeries> list=dao.find(finder);
        return list;
    }
    public List<MediaSeriesTagVo> tagNowList(String seriesId){
        Parameter parameter =Parameter.create().insert("seriesId",seriesId);
        List<MediaSeriesTagVo> findTagNowList = mediaTagMapper.findByNamedQuery(tagNowList,parameter,MediaSeriesTagVo.class);
        return findTagNowList;
    }
    @Transactional
    public void delMedias(String[] ids) {
        for(String id:ids){
            mediaSeriesMapper.delMedias(id);
            //删除resource
            mediaMapper.deleteBySeriesId(id);
            //媒资-功能关联表删除
            mediaSeriesFuncMapper.deleteBySeriesId(id);
        }
    }

    public List<MediaTag> findByDelFlagAndTagNameAndLevelId(String s, String tagName, String levelId) {
        return mediaTagMapper.findByDelFlagAndTagNameAndLevelId(s,tagName,levelId);
    }

    @Transactional
    public void updateIsRecommend(String mediaId, String isRecommend) {
        mediaSeriesMapper.updateIsRecommend(mediaId,isRecommend);
    }

    @Transactional
    public void updateIsHotWord(String mediaId, String isHotWord) {
        mediaSeriesMapper.updateIsHotWord(mediaId,isHotWord);
    }
}
