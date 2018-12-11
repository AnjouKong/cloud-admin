package cn.phenix.cloud.admin.app.brand.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.brand.BrandSpecification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 设备品牌型号管理
 *
 **/
public interface BrandSpecificationMapper extends GenericJpaRepository<BrandSpecification, String> {

    void deleteByBrandId(String id);

    @Modifying
    @Query("UPDATE BrandSpecification SET delFlag='1' where brandId=:id")
    void updateByBrandId(@Param("id") String id);

    List<BrandSpecification> findByBrandIdAndDelFlag(String brandId, String flag);

    List<BrandSpecification> findSpecificationNameByIdIn(List<String> specNames);

    List<BrandSpecification> findBySpecificationNameAndBrandIdAndDelFlag(String specificationName, String brandId, String s);
}
