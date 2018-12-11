package cn.phenix.cloud.admin.app.newMedia.cibn.service;

import cn.phenix.cloud.admin.app.newMedia.cibn.dao.NewCIBNCategoryMapper;
import cn.phenix.cloud.admin.app.newMedia.cibn.dao.NewCIBNListMapper;
import cn.phenix.cloud.admin.app.newMedia.cibn.dao.NewCIBNMediaMapper;
import cn.phenix.cloud.admin.app.newMedia.media.dao.NewMediaCategoryMapper;
import cn.phenix.cloud.admin.app.newMedia.media.dao.NewMediaMapper;
import cn.phenix.cloud.admin.app.newMedia.media.dao.NewMediaSeriesMapper;
import cn.phenix.cloud.core.utils.MyBeanUtils;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.cibn.CIBNMedia;
import cn.phenix.model.app.cibn.CIBNMediaCategory;
import cn.phenix.model.app.cibn.CIBNMediaSeries;
import cn.phenix.model.app.newMedia.NewMedia;
import cn.phenix.model.app.newMedia.NewMediaCategory;
import cn.phenix.model.app.newMedia.NewMediaSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
public class NewCIBNListService {
    @Autowired
    private NewCIBNListMapper newCibnListMapper;
    @Autowired
    private NewMediaSeriesMapper mediaSeriesMapper;
    @Autowired
    private NewCIBNCategoryMapper newCibnCategoryMapper;
    @Autowired
    private NewMediaCategoryMapper mediaCategoryMapper;
    @Autowired
    private NewMediaMapper mediaMapper;
    @Autowired
    private NewCIBNMediaMapper newCibnMediaMapper;

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
        Page<CIBNMediaSeries> page = newCibnListMapper.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    private CompletableFuture<Void> synMediaSeries(List<CIBNMediaSeries> cibnMediaSeries, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            AtomicReference<Integer> size = new AtomicReference<>(0);
            cibnMediaSeries.forEach(s -> {
                NewMediaSeries d = new NewMediaSeries();
                MyBeanUtils.copyProperties(s, d);
                d.setCharge(0);//cibn没有费用字段
                d.setSource("cibn");//cibn没有来源字段
                d.setPublish("0");//默认未发布

                NewMediaSeries ss = mediaSeriesMapper.findById(s.getId()).orElse(null);

                if (ss != null) {
                    d.setUpdateDate(new Date());
                    mediaSeriesMapper.save(d);
                } else {
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
                NewMediaCategory category = new NewMediaCategory();
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
            List<CIBNMedia> mediaList = newCibnMediaMapper.find(finder);
            mediaList.forEach(cibnMedia -> {
                NewMedia media = new NewMedia();
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
        return newCibnListMapper.findByNamedQuery(newCibnListMapper.cibnMediaSeriesList, parameter, CIBNMediaSeries.class);
    }

    private List<CIBNMediaCategory> cibnMediaCategory(String ids) {
        Finder finder = Finder.create("from CIBNMediaCategory where 1=1");
        if (!Strings.isBlank(ids)) {
            String[] idss = ids.split(",");
            finder.search("id", Arrays.asList(idss), Finder.SearchType.IN);
        }
        return newCibnCategoryMapper.find(finder);
    }


    public CIBNMediaSeries findById(String id) {
        return newCibnListMapper.findById(id).orElse(null);
    }
}
