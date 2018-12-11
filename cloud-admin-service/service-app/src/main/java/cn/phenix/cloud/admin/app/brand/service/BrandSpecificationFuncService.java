package cn.phenix.cloud.admin.app.brand.service;

import cn.phenix.cloud.admin.app.brand.dao.BrandSpecificationFuncMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.brand.BrandSpecificationFunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 设备品牌型号管理
 **/
@Service
@Transactional(readOnly = true)
public class BrandSpecificationFuncService extends CoreService<BrandSpecificationFuncMapper, BrandSpecificationFunc> {

    @Autowired
    private BrandSpecificationFuncMapper deviceBrandVersionFuncMapper;


    public List<BrandSpecificationFunc> findFuncs(String parentId) {
        return deviceBrandVersionFuncMapper.findByNamedQuery(deviceBrandVersionFuncMapper.findFuncs,Parameter.create().insert("parentId",parentId),BrandSpecificationFunc.class);
    }

    public void deleteBySpecificationId(String id) {
        deviceBrandVersionFuncMapper.deleteBySpecificationId(id);
    }
}
