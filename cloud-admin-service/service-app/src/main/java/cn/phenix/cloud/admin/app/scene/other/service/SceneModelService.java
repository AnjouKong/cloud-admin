package cn.phenix.cloud.admin.app.scene.other.service;//package cn.phenix.cloud.admin.tenant.verify.service;


import cn.phenix.cloud.admin.app.scene.other.dao.SceneModelMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.scene.SceneModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class SceneModelService extends CoreService<SceneModelMapper, SceneModel> {

    @Autowired
    private SceneModelMapper sceneModelMapper;

    public PageTable<SceneModel> findPage(DataTableParameter parameter, SceneModel sceneModel) {
        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("from SceneModel ");
        finder.whereExcludeDel();
        finder.search("name",sceneModel.getName());
        Page<SceneModel> page = sceneModelMapper.find(finder, pageRequest);
        return new PageTable<>(page);
    }


    public List<SceneModel> findByCode(String code,String id) {
        Finder finder = Finder.create("from SceneModel ");
        finder.whereExcludeDel();
        finder.search("code", code);
        finder.search("id", id, Finder.SearchType.NEQ);
        List<SceneModel> sceneModel=dao.find(finder);
        return  sceneModel;
    }


}
