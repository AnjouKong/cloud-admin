package cn.phenix.cloud.base.plugin;

import cn.phenix.cloud.core.model.BaseModel;
import cn.phenix.cloud.core.model.plugin.IModelInterceptor;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.security.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author xiaobin
 * @create 2017-10-13 下午4:21
 **/
@Component(value = "modelInterceptor")
public class ModelInterceptor implements IModelInterceptor {

    @Override
    public <MODEL extends BaseModel> void add(MODEL baseModel) {
        String userId = SecurityUtils.getPrincipal().getId();
        baseModel.setCreateBy(userId);
        baseModel.setUpdateBy(userId);
        baseModel.setDelFlag(BaseModel.DEL_FLAG_NORMAL);
        baseModel.setCreateDate(new Date());
        baseModel.setUpdateDate(new Date());
        if (StringUtils.isBlank(baseModel.getId()))
            baseModel.setId(IdGen.uuid());
    }

    @Override
    public <MODEL extends BaseModel> void update(MODEL baseModel) {
        String userId = SecurityUtils.getPrincipal().getId();
        baseModel.setUpdateBy(userId);
        baseModel.setUpdateDate(new Date());
    }
}
