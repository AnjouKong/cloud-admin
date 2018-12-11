package cn.phenix.cloud.admin.app.media.cibn.service;

import cn.phenix.cloud.admin.app.media.cibn.dao.CIBNCategoryMapper;
import cn.phenix.cloud.admin.app.media.cibn.dao.CIBNListMapper;
import cn.phenix.cloud.admin.app.media.cibn.dao.CIBNMediaMapper;
import cn.phenix.cloud.admin.app.media.media.dao.MediaCategoryMapper;
import cn.phenix.cloud.admin.app.media.media.dao.MediaMapper;
import cn.phenix.cloud.admin.app.media.media.dao.MediaSeriesMapper;
import cn.phenix.cloud.core.utils.MyBeanUtils;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.cibn.CIBNMedia;
import cn.phenix.model.app.cibn.CIBNMediaCategory;
import cn.phenix.model.app.cibn.CIBNMediaSeries;
import cn.phenix.model.app.media.Media;
import cn.phenix.model.app.media.MediaCategory;
import cn.phenix.model.app.media.MediaSeries;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by 马刚铭 on 2017/8/16.
 */
@Service
@Transactional(readOnly = true)
public class CIBNListService {
    @Autowired
    private CIBNListMapper cibnListMapper;
    @Autowired
    private MediaSeriesMapper mediaSeriesMapper;
    @Autowired
    private CIBNCategoryMapper cibnCategoryMapper;
    @Autowired
    private MediaCategoryMapper mediaCategoryMapper;
    @Autowired
    private MediaMapper mediaMapper;
    @Autowired
    private CIBNMediaMapper cibnMediaMapper;

    public PageTable<CIBNMediaSeries> findPage(DataTableParameter parameter, String categoryId, String sreleaseYear, String ereleaseYear) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CIBNMediaSeries where 1=1 ");
        if (!Strings.isBlank(sreleaseYear)) {
            finder.and("releaseYear").ge(sreleaseYear);
        }
        if (!Strings.isBlank(ereleaseYear)) {
            finder.and("releaseYear").le(ereleaseYear);
        }
        if (!Strings.isBlank(categoryId)) {
            String[] ids = categoryId.split(",");
            finder.and("categoryId").in(Arrays.asList(ids));
        }
        finder.append("order by updateDate desc");
        Page<CIBNMediaSeries> page = cibnListMapper.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    private CompletableFuture<Void> synMediaSeries(List<CIBNMediaSeries> cibnMediaSeries, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            AtomicReference<Integer> size = new AtomicReference<>(0);
            cibnMediaSeries.forEach(s -> {
                MediaSeries d = new MediaSeries();
                MyBeanUtils.copyProperties(s, d);
                d.setCharge(BigDecimal.valueOf(0.00));//cibn没有费用字段
                d.setSource("cibn");//cibn没有来源字段
                d.setPublish("0");//默认未发布
                d.setCornerPrice("2");//默认不收费
                d.setLevelId("1");//默认标准媒资
                d.setStatus("0");//默认初始状态

                MediaSeries ss = mediaSeriesMapper.findById(s.getId()).orElse(null);

                if (ss != null) {
                    //更新时，修改沿用此信息的媒资(此处更新的全部为标准媒资)
                    ss.setUpdateDate(new Date());
                    Finder finder = Finder.create("from MediaSeries ");
                    finder.whereExcludeDel();
                    finder.search("extendId", ss.getId());
                    finder.search("follow", "1");
                    List<MediaSeries> seriesList = mediaSeriesMapper.find(finder);
                    for (MediaSeries m : seriesList) {
                        BeanUtils.copyProperties(ss, m, "id", "extendId", "follow", "keepMySelf", "levelId", "mediaTagsList");
                    }
                    d.setUpdateDate(new Date());
                    seriesList.add(d);
                    mediaSeriesMapper.saveAll(seriesList);
                } else {
                    d.setLevelId("1");
                    d.setCreateDate(new Date());
                    d.setUpdateDate(new Date());
                    mediaSeriesMapper.save(d);
                }
                size.getAndSet(size.get() + 1);
                CompletableFuture.allOf(synMedia(s.getId(), executor)).join();
            });
            return null;
        }, executor);

    }

    private CompletableFuture<Void> synMediaCategory(List<CIBNMediaCategory> cibnMediaCategory, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            cibnMediaCategory.forEach(cibnCategory -> {
                MediaCategory category = new MediaCategory();
                category.setCategoryName(cibnCategory.getCategoryName());
                category.setId(cibnCategory.getId());
                if (mediaCategoryMapper.findById(cibnCategory.getId()).orElse(null) != null) {
                    category.setUpdateDate(new Date());
                    mediaCategoryMapper.save(category);
                } else {
                    category.setCreateDate(new Date());
                    category.setUpdateDate(new Date());
                    mediaCategoryMapper.save(category);
                }
            });
            return null;
        }, executor);

    }

    private CompletableFuture<Void> synMedia(String cibnMediaSeriesId, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            Finder finder = Finder.create("select m from CIBNMedia m where m.seriesId=:seriesId");
            finder.setParam("seriesId", cibnMediaSeriesId);
            List<CIBNMedia> mediaList = cibnMediaMapper.find(finder);
            mediaList.forEach(cibnMedia -> {
                Media media = new Media();
                MyBeanUtils.copyProperties(cibnMedia, media);
                if (mediaMapper.findById(cibnMedia.getId()).orElse(null) != null) {
                    media.setUpdateDate(new Date());
                    mediaMapper.save(media);
                } else {
                    media.setCreateDate(new Date());
                    media.setUpdateDate(new Date());
                    mediaMapper.save(media);
                }
            });
            return null;
        }, executor);
    }

    @Transactional
    public int change(String sreleaseYear, String ereleaseYear, String categoryId) {
        List<CIBNMediaSeries> cibnMediaSeries = search(sreleaseYear, ereleaseYear, categoryId);
        if (!CollectionUtils.isEmpty(cibnMediaSeries)) {

            Executor executor = Executors.newFixedThreadPool(30, r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });
            List<CIBNMediaCategory> cibnMediaCategory = cibnMediaCategory(categoryId);
            CompletableFuture.allOf(synMediaSeries(cibnMediaSeries, executor), synMediaCategory(cibnMediaCategory, executor)).join();
        }
        return 0;
    }

    public List<CIBNMediaSeries> search(String startYear, String endYear, String categoryId) {

        //媒资集查询条件
        Parameter parameter = Parameter.create();
        if (!Strings.isBlank(startYear)) {
            parameter.insert("startYear", startYear);
        }
        if (!Strings.isBlank(endYear)) {
            parameter.insert("endYear", endYear);
        }
        if (!Strings.isBlank(categoryId)) {
            String[] ids = categoryId.split(",");
            parameter.insert("categoryId", Arrays.asList(ids));
        }
        //在sql中会过滤掉已经转换过的"（and id not in SELECT id FROM app_media_series）
        return cibnListMapper.findByNamedQuery(cibnListMapper.cibnMediaSeriesList, parameter, CIBNMediaSeries.class);
    }

    private List<CIBNMediaCategory> cibnMediaCategory(String ids) {
        Finder finder = Finder.create("from CIBNMediaCategory where 1=1");
        if (!Strings.isBlank(ids)) {
            String[] idss = ids.split(",");
            finder.search("id", Arrays.asList(idss), Finder.SearchType.IN);
        }
        return cibnCategoryMapper.find(finder);
    }


    public CIBNMediaSeries findById(String id) {
        return cibnListMapper.findById(id).orElse(null);
    }
}
