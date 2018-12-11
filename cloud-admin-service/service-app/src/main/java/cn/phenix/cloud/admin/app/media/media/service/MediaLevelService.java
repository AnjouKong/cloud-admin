package cn.phenix.cloud.admin.app.media.media.service;

import cn.phenix.cloud.admin.app.media.media.dao.MediaLevelMapper;
import cn.phenix.cloud.admin.app.media.media.dao.MediaSeriesMapper;
import cn.phenix.cloud.admin.app.media.media.dao.ResourceMapper;
import cn.phenix.cloud.admin.app.media.tag.dao.MediaTagMapper;
import cn.phenix.cloud.admin.app.vo.media.MediaSeriesTagVo;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.MyBeanUtils;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.media.Media;
import cn.phenix.model.app.media.MediaLevel;
import cn.phenix.model.app.media.MediaSeries;
import cn.phenix.model.app.media.MediaTag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by fxq on 2017/10/17.
 */
@Service
@Transactional(readOnly = true)
public class MediaLevelService extends CoreService<MediaLevelMapper,MediaLevel>{
    @Autowired
    private MediaLevelMapper mediaLevelMapper;
    @Autowired
    private MediaSeriesMapper mediaSeriesMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private MediaTagMapper mediaTagMapper;
   /* @Autowired
    private ResourceService resourceService;*/

    public PageTable<MediaLevel> findPage(String levelName,String status,DataTableParameter parameter) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from MediaLevel ");
        finder.whereExcludeDel();
        finder.search("levelName",levelName,Finder.SearchType.LIKE);
        finder.search("status",status,Finder.SearchType.EQ);
        finder.append(" order by createDate ");
        Page<MediaLevel> page = mediaLevelMapper.find(finder,pageRequest);

        return new PageTable<>(page);
    }

    public List<MediaLevel> findByDelFlag(String delFlag){
        List<MediaLevel> list= mediaLevelMapper.findByDelFlagOrderByCreateDate(delFlag);
        return list;
    }
    public List<MediaLevel> findTreeLevelNoBase(){

        Finder finder=Finder.create("from MediaLevel ");
        finder.whereExcludeDel();
        finder.search("id","1",Finder.SearchType.NEQ);
        List<MediaLevel> list= dao.find(finder);

        return list;
    }

    @Transactional
    public void updateStatus(String id, String status) {
        mediaLevelMapper.updateStatus(id,status);
    }

    @Transactional
    public List<MediaSeriesTagVo> copy(MediaLevel mediaLevel) throws ExecutionException, InterruptedException {
        List<MediaSeries> seriesList =mediaSeriesMapper.findByLevelId("1");
        List<MediaSeriesTagVo> tagVoList = new ArrayList<>();
        Executor executor = Executors.newFixedThreadPool(30, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        CompletableFuture<Map<String,String>> completableFuture1=CompletableFuture.supplyAsync(()->{
            List<MediaTag> tagList = mediaTagMapper.findByDelFlagAndLevelId("0","1");
            Map<String,String> tagMap = new HashMap<>();//存放新旧id的对应
            tagList.forEach(mediaTag->{
                MediaTag newMediaTag=new MediaTag();
                MyBeanUtils.copyProperties(mediaTag,newMediaTag);
                String newTagId = IdGen.uuid();
                newMediaTag.setId(newTagId);
                newMediaTag.setLevelId(mediaLevel.getId());
                tagMap.put(mediaTag.getId(),newTagId);
                mediaTagMapper.save(newMediaTag);
            });
            return tagMap;
        },executor);

        CompletableFuture<Void> completableFuture2= completableFuture1.thenCompose(tagMap->CompletableFuture.supplyAsync(()->{
            AtomicReference<Integer> size = new AtomicReference<>(0);
            for(int i=0;i<seriesList.size();i++){
                String newSeriesId = IdGen.uuid();//新的媒资id，在资源resource也要用
                MediaSeries series=seriesList.get(i);
                MediaSeries newSeries=new MediaSeries();
                BeanUtils.copyProperties(series,newSeries,"mediaTagsList");
                newSeries.setExtendId(series.getId());
                newSeries.setId(newSeriesId);
                newSeries.setFollow("0");
                newSeries.setLevelId(mediaLevel.getId());
                //2018年3月28日10:03:33 复制过来的媒资不设置热搜和推荐
                newSeries.setIsHotWord("0");
                newSeries.setIsRecommend("0");
                if(series.getMediaTagsList().size()>0){
                    for(int j=0;j<series.getMediaTagsList().size();j++){
                        List<MediaSeriesTagVo> tagVoList2 = mediaTagMapper.findByNamedQuery(mediaTagMapper.findSeriesTagVo,
                                Parameter.create().insert("tagId",series.getMediaTagsList().get(j).getId()).insert("seriesId",series.getId()),MediaSeriesTagVo.class);
                        MediaSeriesTagVo tagVo =tagVoList2.get(0);
                        String oldTagId = series.getMediaTagsList().get(j).getId();
                        tagVo.setTagId(tagMap.get(oldTagId));
                        tagVo.setMediaSeriesId(newSeriesId);
                        tagVoList.add(tagVo);
                    }
                }
                mediaSeriesMapper.save(newSeries);
                size.getAndSet(size.get() + 1);
                CompletableFuture.allOf(synResource(newSeriesId,series,executor)).join();
            }
            //返回结果
            return null;
        },executor));
        CompletableFuture.allOf(completableFuture1,completableFuture2).join();
        return tagVoList;
    }

    private CompletableFuture<Void> synResource(String newSeriesId ,MediaSeries series, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            List<Media> mediaList = resourceMapper.findBySeriesId(series.getId());
            if(mediaList.size()>0){
                mediaList.forEach(media->{
                    Media newMedia=new Media();
                    MyBeanUtils.copyProperties(media,newMedia);
                    newMedia.setId(IdGen.uuid());
                    newMedia.setSeriesId(newSeriesId);
                    resourceMapper.save(newMedia);
                });
            }
            return null;
        }, executor);
    }
    private CompletableFuture<Void> synTagList(String newSeriesId ,MediaSeries series, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            List<Media> mediaList = resourceMapper.findBySeriesId(series.getId());
            if(mediaList.size()>0){
                mediaList.forEach(media->{
                    Media newMedia=new Media();
                    MyBeanUtils.copyProperties(media,newMedia);
                    newMedia.setId(IdGen.uuid());
                    newMedia.setSeriesId(newSeriesId);
                    resourceMapper.save(newMedia);
                });
            }
            return null;
        }, executor);
    }
    private CompletableFuture<List<MediaSeriesTagVo>> synTags (MediaSeries series,Map<String,String> tagMap,String newSeriesId, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            List<MediaSeriesTagVo> tagVoList = new ArrayList<>();
            if(series.getMediaTagsList().size()>0){
                for(int j=0;j<series.getMediaTagsList().size();j++){

                    List<MediaSeriesTagVo> tagVoList2 = mediaTagMapper.findByNamedQuery(mediaTagMapper.findSeriesTagVo,
                            Parameter.create().insert("tagId",series.getMediaTagsList().get(j).getId()).insert("seriesId",series.getId()),MediaSeriesTagVo.class);
                    MediaSeriesTagVo tagVo =tagVoList2.get(0);
                    String oldTagId = series.getMediaTagsList().get(j).getId();
                    tagVo.setTagId(tagMap.get(oldTagId));
                    tagVo.setMediaSeriesId(newSeriesId);
                    tagVoList.add(tagVo);
                }
            }
            return tagVoList;
        }, executor);
    }

@Transactional
public void copyLocation(List<MediaSeriesTagVo> tagVoList) {
    for(MediaSeriesTagVo tagVo:tagVoList){
        mediaTagMapper.updateByNamedQuery(Parameter.create(mediaTagMapper.insertData).insert("tagId",tagVo.getTagId()).insert("seriesId",tagVo.getMediaSeriesId()).insert("location",tagVo.getLocation()));
    }
}
    @Transactional
    public void delCascade(String id) {
        mediaLevelMapper.deleteById(id);
        mediaSeriesMapper.deleteByLevelId(id);
        mediaTagMapper.deleteByLevelId(id);
        List<MediaSeries> seriesList = mediaSeriesMapper.findByLevelId(id);
        if(seriesList.size()>0){
            for(MediaSeries mediaSeries:seriesList){
                resourceMapper.deleteBySeriesId(mediaSeries.getId());
            }
        }

    }

}
