package cn.phenix.cloud.base.utils;


import cn.phenix.cloud.core.security.Principal;
import cn.phenix.cloud.core.utils.JsonUtils;
import cn.phenix.cloud.core.utils.Strings;

import java.io.*;
import java.util.Random;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
public class StringUtil {
    /**
     * 从seesion获取uid
     *
     * @return
     */
    public static String getUid() {
        try {
            Principal principal = cn.phenix.cloud.security.SecurityUtils.getPrincipal();
            if (principal != null) {
                return Strings.sNull(principal.getId());
            }
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * 从seesion获取用户名
     *
     * @return
     */
    public static String getUsername() {
        try {
            Principal principal = cn.phenix.cloud.security.SecurityUtils.getPrincipal();
            if (principal != null) {
                return Strings.sNull(principal.getName());
            }
        } catch (Exception e) {

        }
        return "";
    }

    public static byte[] getBytesFromObject(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bo = null;
        ObjectOutputStream oo = null;
        try {
            bo = new ByteArrayOutputStream();
            oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
        } finally {
            if (bo != null) {
                bo.close();
            }
            if (oo != null) {
                oo.close();
            }
        }
        return bytes;
    }

    public static Object getObjectFromByteArray(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayInputStream bi = null;
        ObjectInputStream oi = null;
        try {
            if (bytes == null) {
                return null;
            }
            bi = new ByteArrayInputStream(bytes);
            oi = new ObjectInputStream(bi);
            obj = oi.readObject();
        } finally {
            if (bi != null) {
                bi.close();
            }
            if (oi != null) {
                oi.close();
            }
        }
        return obj;
    }

    /**
     * 去掉URL中?后的路径
     *
     * @param p
     * @return
     */
    public static String getPath(String p) {
        if (Strings.sNull(p).contains("?")) {
            return p.substring(0, p.indexOf("?"));
        }
        return Strings.sNull(p);
    }

    /**
     * 获得父节点ID
     *
     * @param s
     * @return
     */
    public static String getParentId(String s) {
        if (!Strings.isEmpty(s) && s.length() > 4) {
            return s.substring(0, s.length() - 4);
        }
        return "";
    }

    /**
     * 得到n位随机数
     *
     * @param s
     * @return
     */
    public static String getRndNumber(int s) {
        Random ra = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s; i++) {
            sb.append(String.valueOf(ra.nextInt(8)));
        }
        return sb.toString();
    }

    /**
     * 判断是否以字符串开头
     *
     * @param str
     * @param s
     * @return
     */
    public boolean startWith(String str, String s) {
        return Strings.sNull(str).startsWith(Strings.sNull(s));
    }

    /**
     * 判断是否包含字符串
     *
     * @param str
     * @param s
     * @return
     */
    public boolean contains(String str, String s) {
        return Strings.sNull(str).contains(Strings.sNull(s));
    }

    /**
     * 将对象转为JSON字符串（页面上使用）
     *
     * @param obj
     * @return
     */
    public String toJson(Object obj) {
        return JsonUtils.safeObjectToJson(obj);
    }

    public String getStatusInfo(String status) {
        return "1".equals(status) ? "是" : "否";
    }
}
