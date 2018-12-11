package cn.phenix.cloud.admin.app.media.media.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.media.MediaCategory;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface MediaCategoryMapper extends GenericJpaRepository<MediaCategory,String> {


    List<MediaCategory> findByCategoryNameAndDelFlag(String categoryName, String delFlag);

    List<MediaCategory> findByCategoryTypeAndDelFlag(int categoryType, String delFlag);

    List<MediaCategory> findByDelFlag(String delFlag);
}

