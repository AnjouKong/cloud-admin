package cn.phenix.cloud.admin.app.newMedia.cibn.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.cibn.CIBNMediaCategory;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface NewCIBNCategoryMapper extends GenericJpaRepository<CIBNMediaCategory,String> {

    List<CIBNMediaCategory> findByDelFlag(String s);
}

