package cn.phenix.cloud.utils;

import cn.phenix.cloud.core.model.TreeEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2017-09-29 下午4:27
 **/
public class TreeUtils {

    public static Map<String, Object> treeMap(TreeEntity entity) {
        Map<String, Object> obj = new HashMap<>();
        obj.put("id", entity.getId());
        obj.put("text", entity.getName());
        obj.put("children", true);
        return obj;
    }
}
