package cn.phenix.cloud.admin.app.brand.service;

import cn.phenix.cloud.admin.app.brand.dao.BrandMapper;
import cn.phenix.cloud.admin.app.brand.dao.BrandSpecificationMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.brand.Brand;
import cn.phenix.model.app.brand.BrandSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 设备品牌管理
 **/
@Service
@Transactional(readOnly = true)
public class BrandService extends CoreService<BrandMapper, Brand> {

    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private BrandSpecificationMapper brandSpecificationMapper;


    public PageTable<Brand> findPage( Brand deviceBrand,DataTableParameter parameter){
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select m from Brand m where m.delFlag=:delFlag  ")
                .setParam("delFlag", Brand.DEL_FLAG_NORMAL);
        finder.search("brandName",deviceBrand.getBrandName(), Finder.SearchType.LIKE);
        Page<Brand> page= brandMapper.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public Integer findByBrandName(String brandName) {
        Finder finder = Finder.create("from  Brand m where m.delFlag=:delFlag and m.brandName=:brandName ")
                .setParam("delFlag", Brand.DEL_FLAG_NORMAL).setParam("brandName", brandName);
        List<Brand> deviceBrandList = brandMapper.find(finder);

        if(deviceBrandList!=null)
            return deviceBrandList.size();

        return 0;
    }

    @Transactional
    public void deleteCascade(String id) {

        dao.updateDelFlag(id);
        brandSpecificationMapper.updateByBrandId(id);
    }

    public List<Brand> findByDelFlag(String delFlag) {
        List<Brand> list= brandMapper.findByDelFlagOrderByCreateDate(delFlag);
        return list;
    }

    public List<Brand> findBrandNameByIdIn(List<String> specNames) {
        return dao.findBrandNameByIdIn(specNames);
    }
}
