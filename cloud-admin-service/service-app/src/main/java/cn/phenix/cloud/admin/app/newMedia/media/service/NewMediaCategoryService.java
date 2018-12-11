package cn.phenix.cloud.admin.app.newMedia.media.service;

import cn.phenix.cloud.admin.app.newMedia.media.dao.NewMediaCategoryMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaCategory;
import cn.phenix.model.app.newMedia.NewMediaCategory;
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
public class NewMediaCategoryService extends CoreService<NewMediaCategoryMapper,NewMediaCategory>{


    public PageTable<NewMediaCategory> findPage(DataTableParameter parameter, NewMediaCategory category) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from NewMediaCategory where delFlag=:delFlag ")
                .setParam("delFlag", MediaCategory.DEL_FLAG_NORMAL);
        finder.search("categoryName",category.getCategoryName(), Finder.SearchType.LIKE);

        Page<NewMediaCategory> page = dao.find(finder,pageRequest);

        return new PageTable<>(page);
    }


    public List<NewMediaCategory> findByCategoryNameAndDelFlag(String categoryName, String delFlag) {
        return dao.findByCategoryNameAndDelFlag(categoryName,delFlag);
    }



    public List<NewMediaCategory> findByCategoryTypeAndDelFlag(int categoryType, String delFlag) {
       return  dao.findByCategoryTypeAndDelFlag(categoryType,delFlag);
    }


    public List<NewMediaCategory> findByDelFlag(String delFlag) {
        return  dao.findByDelFlag(delFlag);
    }

}
