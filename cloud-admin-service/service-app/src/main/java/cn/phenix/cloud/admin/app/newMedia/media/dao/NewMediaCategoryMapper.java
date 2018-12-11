package cn.phenix.cloud.admin.app.newMedia.media.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.newMedia.NewMediaCategory;

import java.util.List;

/**
 * Created by fxq on 2016/12/22.
 */
public interface NewMediaCategoryMapper extends GenericJpaRepository<NewMediaCategory,String> {


    List<NewMediaCategory> findByCategoryNameAndDelFlag(String categoryName, String delFlag);

    List<NewMediaCategory> findByCategoryTypeAndDelFlag(int categoryType, String delFlag);

    List<NewMediaCategory> findByDelFlag(String delFlag);
}

