package cn.phenix.cloud.admin.app.brand.service;

import cn.phenix.cloud.admin.app.brand.dao.BrandSpecificationMapper;
import cn.phenix.cloud.admin.sys.service.SysDictService;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.brand.BrandSpecification;
import cn.phenix.model.app.brand.BrandSpecificationFunc;
import cn.phenix.model.sys.SysDict;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 设备品牌型号管理
 **/
@Service
@Transactional(readOnly = true)
public class BrandSpecificationService extends CoreService<BrandSpecificationMapper, BrandSpecification> {

    @Autowired
    private BrandSpecificationMapper brandSpecificationMapper;
    @Autowired
    private BrandSpecificationFuncService brandSpecificationFuncService;
    @Autowired
    private SysDictService sysDictService;

    public PageTable<BrandSpecification> findPage( BrandSpecification deviceBrandVersion,DataTableParameter parameter){
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select m from BrandSpecification m where m.delFlag=:delFlag  ")
                .setParam("delFlag", BrandSpecification.DEL_FLAG_NORMAL);
        finder.search("m.brandId",deviceBrandVersion.getBrandId());
        finder.search("m.specificationName",deviceBrandVersion.getSpecificationName(), Finder.SearchType.LIKE);

        Page<BrandSpecification> page= brandSpecificationMapper.find(finder, pageRequest);
        return new PageTable<>(page);
    }


    @Transactional
    public void addDo(BrandSpecification deviceBrandVersion) {
        dao.save(deviceBrandVersion);
        //维护关联表
        if(StringUtils.isNoneEmpty(deviceBrandVersion.getFuncIds())){
            String[] funcIds = deviceBrandVersion.getFuncIds().split(",");
            for(int i=0;i<funcIds.length;i++){
                SysDict sysDict =sysDictService.get(funcIds[i]);
                BrandSpecificationFunc brandSpecificationFunc = new BrandSpecificationFunc();
                brandSpecificationFunc.setFuncId(funcIds[i]);
                brandSpecificationFunc.setSpecificationId(deviceBrandVersion.getId());
                brandSpecificationFunc.setFuncName(sysDict.getName());
                brandSpecificationFunc.setParentId(sysDict.getParentId());
                brandSpecificationFuncService.save(brandSpecificationFunc);
            }
        }
    }

    @Transactional
    public void delete(String id) {
        dao.deleteById(id);
        //删除关联表
        brandSpecificationFuncService.deleteBySpecificationId(id);
    }

    @Transactional
    public void deletes(String[] ids) {
        for(String id :ids){
            dao.deleteById(id);
            //删除关联表
            brandSpecificationFuncService.deleteBySpecificationId(id);
        }
    }

    @Transactional
    public void editDo(BrandSpecification deviceBrandVersion) {
        brandSpecificationFuncService.deleteBySpecificationId(deviceBrandVersion.getId());
        //维护关联表
        if(StringUtils.isNoneEmpty(deviceBrandVersion.getFuncIds())){
            String[] funcIds = deviceBrandVersion.getFuncIds().split(",");
            for(int i=0;i<funcIds.length;i++){
                SysDict sysDict =sysDictService.get(funcIds[i]);
                BrandSpecificationFunc brandSpecificationFunc = new BrandSpecificationFunc();
                brandSpecificationFunc.setFuncId(funcIds[i]);
                brandSpecificationFunc.setSpecificationId(deviceBrandVersion.getId());
                brandSpecificationFunc.setFuncName(sysDict.getName());
                brandSpecificationFunc.setParentId(sysDict.getParentId());
                brandSpecificationFuncService.save(brandSpecificationFunc);
            }
        }
        dao.save(deviceBrandVersion);
    }

    public List<BrandSpecification> findByBrandIdAndDelFlag(String brandId,String flag) {
        return brandSpecificationMapper.findByBrandIdAndDelFlag(brandId,flag);
    }

    public List<BrandSpecification> findSpecificationNameByIdIn(List<String> specNames) {
        return brandSpecificationMapper.findSpecificationNameByIdIn(specNames);
    }

    public List<BrandSpecification> findBySpecificationNameAndBrandIdAndDelFlag(String specificationName, String brandId, String s) {
        return dao.findBySpecificationNameAndBrandIdAndDelFlag(specificationName,brandId,s);
    }
}
