package cn.phenix.cloud.admin.app.brand.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.brand.BrandSpecificationFunc;

/**
 * 型号功能
 *
 **/
public interface BrandSpecificationFuncMapper extends GenericJpaRepository<BrandSpecificationFunc, String> {

    String findFuncs="findFuncs";

    void deleteBySpecificationId(String id);
}
