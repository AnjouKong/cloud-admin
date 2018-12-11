package cn.phenix.cloud.admin.app.media.cibn.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.cibn.CIBNMediaSeries;

/**
 * Created by mgm
 */
public interface CIBNListMapper extends GenericJpaRepository<CIBNMediaSeries,String> {

//    int change(Cnd cnd, String ids);

    String cibnMediaSeriesList = "cibnMediaSeriesList";

}
