package cn.phenix.model.ui.v1.utils;

import cn.phenix.cloud.core.utils.MyBeanUtils;
import cn.phenix.model.ui.v1.launcher.ViewStyle;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * hash值
 *
 * @author xiaobin
 * @create 2018-01-08 下午3:31
 **/
public class HashCodeUtils {
/*

    public static int getHashCode(Object... values) {
        Integer hashCode = 0;
        for (Object value : values) {
            if (value == null)
                continue;
            if (value instanceof String) {
                hashCode += value.hashCode();
            } else if (value instanceof Integer) {
                hashCode += (Integer) value;
            }
        }
        return hashCode;
    }
*/

    public static int getHashCode(Object object, String idValue) {
        Map<String, Object> map = MyBeanUtils.describe(new ViewStyle());
        Integer hashCode = getHashCode(object);
        if (StringUtils.isNotBlank(idValue)) {
            hashCode += idValue.hashCode();
        }
        return hashCode;
    }

    public static int getHashCode(Object object) {
        Map<String, Object> map = MyBeanUtils.describe(object);
        Integer hashCode = 0;
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value == null)
                continue;
            if (value instanceof String) {
                if (!value.toString().equals("")) {
                    hashCode += value.hashCode();
                }
            } else if (value instanceof Integer) {
                hashCode += (Integer) value;
            } else if (value instanceof Double) {
                hashCode += value.hashCode();
            }
        }
        return hashCode;
    }

}
