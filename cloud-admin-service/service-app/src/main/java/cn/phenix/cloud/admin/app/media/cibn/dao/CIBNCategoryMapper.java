package cn.phenix.cloud.admin.app.media.cibn.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.cibn.CIBNMediaCategory;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface CIBNCategoryMapper  extends GenericJpaRepository<CIBNMediaCategory,String> {

    List<CIBNMediaCategory> findByDelFlag(String s);
}

