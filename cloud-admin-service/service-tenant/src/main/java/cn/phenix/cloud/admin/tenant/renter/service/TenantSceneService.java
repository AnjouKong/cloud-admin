package cn.phenix.cloud.admin.tenant.renter.service;

import cn.phenix.cloud.admin.tenant.renter.dao.TenantSceneMapper;
import cn.phenix.cloud.admin.tenant.renter.vo.TenantSceneVo;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.renter.TenantScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class TenantSceneService extends CoreService<TenantSceneMapper, TenantScene> {

    @Autowired
    private MongoTemplate mongoTemplate;


    public PageTable<TenantScene> findPage(DataTableParameter parameter, TenantScene tenantScene) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from TenantScene ");
        finder.whereExcludeDel();
        finder.search("tenantId", tenantScene.getTenantId());
        finder.search("sceneName", tenantScene.getSceneName(), Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc");
        Page<TenantScene> page = dao.find(finder, pageRequest);

        return new PageTable<>(page);
    }

    public List<TenantScene> findBySceneId(String sceneId) {
        Finder finder = Finder.create("from TenantScene ");
        finder.whereExcludeDel();
        finder.search("sceneId", sceneId);
        List<TenantScene> sceneList = dao.find(finder);

        return sceneList;
    }

    public TenantSceneVo findOne(String id) {
        if (Strings.isBlank(id)) return null;
        Query query = new Query(Criteria.where("_id").is(id));
        TenantSceneVo sceneSaveVo = mongoTemplate.findOne(query, TenantSceneVo.class, "tenant_scene");
        return sceneSaveVo;
    }

//    @Transactional
//    public void delete(String id) {
//        Query query = new Query(Criteria.where("_id").is(id));
//        mongoTemplate.remove(query, TenantSceneVo.class, "tenant_scene");
//    }

    @Transactional
    public void editMongodb(TenantSceneVo tenantSceneVo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(tenantSceneVo.getId()));
        Update update = new Update();
        update.set("appUI", tenantSceneVo.getAppUI());
        update.set("hashcode", tenantSceneVo.getHashcode());
        mongoTemplate.upsert(query, update, "tenant_scene");
    }

    @Transactional
    public void saveMongodb(TenantSceneVo sceneLayoutVo) {
        mongoTemplate.save(sceneLayoutVo, "tenant_scene");
    }

    public List<TenantScene> findByDelFlagAndTenantId(String delFlag,String id) {
        return dao.findByDelFlagAndTenantId(delFlag,id);
    }

}
