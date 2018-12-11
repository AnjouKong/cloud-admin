package cn.phenix.cloud.admin.app.newMedia.cibn.service;

import cn.phenix.cloud.admin.app.newMedia.cibn.dao.NewCIBNCategoryMapper;
import cn.phenix.model.app.cibn.CIBNMediaCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 马刚铭 on 2017/8/16.
 */
@Service
@Transactional(readOnly = true)
public class NewCIBNCategoryService {
    @Autowired
    private NewCIBNCategoryMapper newCibnCategoryMapper;

    public List<CIBNMediaCategory> findByDelFlag() {
        return newCibnCategoryMapper.findByDelFlag("0");
    }
}
