package cn.phenix.cloud.admin.app.scene.basic.service;


import cn.phenix.cloud.admin.app.scene.basic.dao.SceneBasicInfoMapper;
import cn.phenix.cloud.admin.app.vo.scene.SceneSaveVo;
import cn.phenix.cloud.core.security.Principal;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.scene.SceneBasicInfo;
import cn.phenix.model.app.scene.SceneBasicInfoEditor;
import cn.phenix.model.app.scene.SceneBasicInfoReader;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class SceneBasicInfoService extends CoreService<SceneBasicInfoMapper, SceneBasicInfo> {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SceneBasicInfoReaderService sceneBasicInfoReaderService;
    @Autowired
    private SceneBasicInfoEditorService sceneBasicInfoEditorService;

    public PageTable<SceneBasicInfo> findPage(DataTableParameter parameter, SceneBasicInfo sceneBasicInfo, Principal principal) {
        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("select s from SceneBasicInfo s");
        finder.whereExcludeDel("s");

        if (!principal.isAdmin()) {

            List<SceneBasicInfoReader> readerList = sceneBasicInfoReaderService.findByReaderId(principal.getId());
            List<SceneBasicInfoEditor> editorList = sceneBasicInfoEditorService.findByEditorId(principal.getId());
            List<String> ids = Lists.newArrayList();
            ids.add("-1");//防止空异常
            if (readerList != null) {
                for (SceneBasicInfoReader reader : readerList) {
                    ids.add(reader.getTemplateId());
                }
            }
            if (editorList != null) {
                for (SceneBasicInfoEditor editor : editorList) {
                    ids.add(editor.getTemplateId());
                }
            }
            finder.append(" and ( s.id in (:id) or s.createBy=:createBy )").setParam("id", ids).setParam("createBy", principal.getId());
        }

        finder.search("s.categoryId", sceneBasicInfo.getCategoryId());
        finder.search("s.sceneName", sceneBasicInfo.getSceneName(), Finder.SearchType.LIKE);
        finder.append(" order by s.createDate desc ");
        Page<SceneBasicInfo> page = dao.find(finder, pageRequest);

        for (int i = 0; i < page.getContent().size(); i++) {
            SceneBasicInfo info = page.getContent().get(i);
            if (info.getCreateBy().equals(principal.getId()) || principal.isAdmin()) {
                info.setAuth("edit");
                continue;
            }
            if (sceneBasicInfoReaderService.isReader(principal.getId(), info.getId())) {
                info.setAuth("view");
            }
            if (sceneBasicInfoEditorService.isEditor(principal.getId(), info.getId())) {
                info.setAuth("edit");
            }

        }

        return new PageTable<>(page);
    }

    @Transactional
    public void save(SceneSaveVo vo) {
        mongoTemplate.save(vo, "scene");
    }

    public SceneSaveVo findOne(String id) {
        if (Strings.isBlank(id)) return null;
        Query query = new Query(Criteria.where("_id").is(id));
        SceneSaveVo sceneSaveVo = mongoTemplate.findOne(query, SceneSaveVo.class, "scene");
        return sceneSaveVo;
    }


}
