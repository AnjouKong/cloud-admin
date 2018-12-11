package cn.phenix.cloud.admin.app.scene.basic.service;//package cn.phenix.cloud.admin.tenant.verify.service;


import cn.phenix.cloud.admin.app.scene.basic.dao.SceneVersionInfoMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.scene.SceneVersionInfo;
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
public class SceneVersionInfoService extends CoreService<SceneVersionInfoMapper, SceneVersionInfo> {


    public PageTable<SceneVersionInfo> findPage(DataTableParameter parameter, SceneVersionInfo sceneVersionInfo) {
        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("from SceneVersionInfo ");
        finder.whereExcludeDel();
        finder.search("basicId", sceneVersionInfo.getBasicId());
        finder.append(" order by version desc ");

        Page<SceneVersionInfo> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


    /**
     * 根据场景基本信息id获取最新的版本号
     *
     * @param basicId 场景基本信息id
     * @return
     */
    public SceneVersionInfo findSceneVersionInfoByBasicId(String basicId, String publish) {
        Finder finder = Finder.create("from SceneVersionInfo ");
        finder.whereExcludeDel();
        finder.search("basicId", basicId);
        finder.search("publish", publish);
        finder.append(" order by version desc");
        List<SceneVersionInfo> infoList = dao.find(finder);
        if (infoList != null && infoList.size() > 0) return infoList.get(0);
        else return null;
    }


}
