package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsChannelMapper;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.model.app.cms.CmsChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CmsChannelService extends CoreService<CmsChannelMapper, CmsChannel> {

    public List<CmsChannel> findByParentIdAndType(String parentId, String type, String disabled) {
        Finder finder = Finder.create("from CmsChannel ");
        finder.whereExcludeDel();
        if (!Strings.isBlank(parentId)) {
            finder.search("parentId", parentId);
        } else {
            finder.append(" and (parentId is null or parentId = '') ");
        }
        if (!Strings.isBlank(disabled)) {
            finder.search("disabled", false);
        }
        finder.search("type", type);
        finder.append(" order by location ");
        return dao.find(finder);
    }

    public boolean isHasChildren(String id) {
        Finder finder = Finder.create("from CmsChannel ");
        finder.whereExcludeDel();
        finder.search("parentId", id);
        finder.search("disabled", false);
        List<CmsChannel> list = dao.find(finder);
        if (list != null && list.size() > 0) return true;
        return false;
    }


    @Transactional
    public void updateHasChildren(String pid, boolean hasChild) {
        Finder finder = Finder.create("from CmsChannel ");
        finder.whereExcludeDel();
        finder.search("parentId", pid);
        List<CmsChannel> list = dao.find(finder);
        if (list == null || list.size() == 0) {
            dao.updateHasChildren(hasChild, pid);
        }
    }

    @Transactional
    public void saveAndUpdateHasChildren(CmsChannel channel) {
        save(channel);
        if (!Strings.isEmpty(channel.getParentId())) {
            boolean b = true;
            dao.updateHasChildren(b, channel.getParentId());
        }
    }

    @Transactional
    public void updateById(String menuId, boolean disable) {
        dao.updateById(menuId, disable);
    }

    @Transactional
    public void updateLocationById(int i, String s) {
        dao.updateLocationById(i, s);
    }

    @Transactional
    public void updateAllLocation() {
        dao.updateAllLocation();
    }
}
