package cn.phenix.cloud.admin.app.media.cibn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.phenix.cloud.admin.app.media.cibn.dao.CIBNCategoryMapper;
import cn.phenix.model.app.cibn.CIBNMediaCategory;

/**
 * Created by 马刚铭 on 2017/8/16.
 */
@Service
@Transactional(readOnly = true)
public class CIBNCategoryService {
    @Autowired
    private CIBNCategoryMapper cibnCategoryMapper ;

    public List<CIBNMediaCategory> findByDelFlag() {
        return cibnCategoryMapper.findByDelFlag("0");
    }
}
