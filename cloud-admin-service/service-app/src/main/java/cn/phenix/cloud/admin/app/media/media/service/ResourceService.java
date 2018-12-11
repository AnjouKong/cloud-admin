package cn.phenix.cloud.admin.app.media.media.service;

import cn.phenix.cloud.admin.app.media.media.dao.ResourceMapper;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.MyBeanUtils;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 马刚铭 on 2017/8/16.
 */
@Service
@Transactional(readOnly = true)
public class ResourceService extends CoreService<ResourceMapper,Media>{
    @Autowired
    private ResourceMapper resourceMapper;

    public PageTable<Media> findPage(String seriesID, String programCode, String programVolumnCount, DataTableParameter parameter) {

        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("from Media where 1=1");
        finder.search("seriesID",seriesID,Finder.SearchType.EQ);
        finder.search("programCode",programCode,Finder.SearchType.EQ);
        finder.search("programVolumnCount",programVolumnCount,Finder.SearchType.EQ);

        Page<Media> page = resourceMapper.find(finder,pageRequest);
        return  new PageTable<>(page);
    }

    @Transactional
    public void copyResources(String oldSeriesId, String newSeriesId) {
        List<Media> mediaList = resourceMapper.findBySeriesId(oldSeriesId);
        for(Media media:mediaList){
            Media newMedia=new Media();
            MyBeanUtils.copyProperties(media,newMedia);
            newMedia.setId(IdGen.uuid());
            newMedia.setSeriesId(newSeriesId);
            resourceMapper.save(newMedia);
        }
    }
}
