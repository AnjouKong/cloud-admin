package cn.phenix.cloud.admin.app.brand.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.brand.Brand;

import java.util.List;


/**
 * 设备品牌管理
 *
 **/
public interface BrandMapper extends GenericJpaRepository<Brand, String> {

    List<Brand> findByDelFlagOrderByCreateDate(String delFlag);

    List<Brand> findBrandNameByIdIn(List<String> specNames);
}
