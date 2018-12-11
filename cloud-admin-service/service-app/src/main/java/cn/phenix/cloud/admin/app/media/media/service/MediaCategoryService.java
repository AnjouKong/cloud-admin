package cn.phenix.cloud.admin.app.media.media.service;

import cn.phenix.cloud.admin.app.media.media.dao.MediaCategoryMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaCategory;
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
public class MediaCategoryService extends CoreService<MediaCategoryMapper,MediaCategory>{
    @Autowired
    private MediaCategoryMapper mediaCategoryMapper;

    public PageTable<MediaCategory> findPage(DataTableParameter parameter, MediaCategory category) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from MediaCategory where delFlag=:delFlag and categoryType=:categoryType")
                .setParam("delFlag", MediaCategory.DEL_FLAG_NORMAL)
                .setParam("categoryType",1);
        finder.search("categoryName",category.getCategoryName(), Finder.SearchType.LIKE);

        Page<MediaCategory> page = mediaCategoryMapper.find(finder,pageRequest);

        return new PageTable<>(page);
    }


    public List<MediaCategory> findByCategoryNameAndDelFlag(String categoryName, String delFlag) {
        return mediaCategoryMapper.findByCategoryNameAndDelFlag(categoryName,delFlag);
    }



    public List<MediaCategory> findByCategoryTypeAndDelFlag(int categoryType, String delFlag) {
       return  mediaCategoryMapper.findByCategoryTypeAndDelFlag(categoryType,delFlag);
    }


    public List<MediaCategory> findByDelFlag(String delFlag) {
        return  mediaCategoryMapper.findByDelFlag(delFlag);
    }

}
