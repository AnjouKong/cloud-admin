package cn.phenix.cloud.controller.tenant.renter;

import cn.phenix.cloud.admin.tenant.pay.vo.ResponseJson;

import cn.phenix.cloud.core.utils.DateUtils;
import cn.phenix.cloud.core.utils.JsonUtils;
import cn.phenix.cloud.core.utils.MD5Util;
import cn.phenix.cloud.rpc.cache.vo.CacheVo;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TenantCacheTest {


    private void packageCacheVo(String tenantId, CacheVo cacheVo) {
        cacheVo.setAppId("111");
        cacheVo.setTenantId("1012");
        cacheVo.setTimestamp(System.currentTimeMillis()+"");
        StringBuffer sb = new StringBuffer();
        sb.append(cacheVo.getAppId()).append(cacheVo.getTimestamp()).append(cacheVo.getTenantId()).append("#").append(""); //混淆码
        cacheVo.setSignature(MD5Util.getMD5String(sb.toString()));

    }

    @Test
    public void testClear(){
        CacheVo cacheVo = new CacheVo();
        this.packageCacheVo("qqqq", cacheVo);
        //创建一个头部对象
        HttpHeaders headers = new HttpHeaders();
        //设置contentType
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        //设置我们的请求信息，第一个参数为请求Body,第二个参数为请求头信息
        HttpEntity<String> strEntity = new HttpEntity(JsonUtils.writeObjectToJson(cacheVo), headers);
        RestTemplate restTemplate = new RestTemplate();
        //请求接口
        String restJson = restTemplate.postForObject("http://192.168.16.115:2100/service-media/api/cache/clear/", strEntity, String.class, cacheVo);

        ResponseJson json = JsonUtils.readObjectByJson(restJson, ResponseJson.class);
    }


    public static void main(String[] args) throws Exception {

        FileReader reader = new FileReader("C:\\Users\\happy\\Desktop\\BaiduMap_cityCode_1102.txt");
        BufferedReader br = new BufferedReader(reader);

        String string = null;

        while ((string = br.readLine()) != null) {
            String[] pattern =string.split(",");
            String sql = "UPDATE `sys_area` a SET a.areaCode='"+pattern[0]+"' WHERE a.`name`='"+pattern[1]+"';";
            System.out.println(sql);
         }
        br.close();
        reader.close();
    }
}
