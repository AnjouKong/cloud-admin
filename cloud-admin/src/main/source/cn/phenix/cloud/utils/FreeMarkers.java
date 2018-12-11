package cn.phenix.cloud.utils;

import cn.phenix.cloud.core.utils.Exceptions;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * FreeMarkers工具类
 *
 * @author xiaobin
 **/
public class FreeMarkers {

    /**
     * 静态内部类，创建freeMarker 配置，实例化工具类
     */
    private static class LazyHolder {

        // 创建 freeMarker 配置 ，2.3.24  引入的freemarker 版本号
        private static final Configuration config = new Configuration(Configuration.VERSION_2_3_25);
    }

    // FreeMarker 配置
    private static Configuration cfg;

    public static void builder() {
        if (null == cfg) {
            // 创建 freeMarker 配置
            cfg = LazyHolder.config;
            // 设置编码格式
            cfg.setDefaultEncoding("UTF-8");
            // templatePath 指的是模板所在路径
            //
            cfg.setClassForTemplateLoading(FreeMarkers.class, "/");
        }
    }

    public static String renderString(String templateString, Map<String, ?> model) {
        try {
            StringWriter result = new StringWriter();
            Template t = new Template("name", new StringReader(templateString), new Configuration(Configuration.VERSION_2_3_25));
            t.process(model, result);
            return result.toString();
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    public static String render(String view, Map<String, Object> model) {
        try {
            Template template = cfg.getTemplate("freemarker/" + view + ".ftl");
            return renderTemplate(template, model);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    public static String renderTemplate(Template template, Object model) {
        try {
            StringWriter result = new StringWriter();
            template.process(model, result);
            return result.toString();
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    public static Configuration buildConfiguration(String directory) throws IOException {
        Configuration cfg = new Configuration();
        Resource path = new DefaultResourceLoader().getResource(directory);
        cfg.setDirectoryForTemplateLoading(path.getFile());
        return cfg;
    }

    public static void main(String[] args) throws IOException {
        /*Map<String, Integer> model = com.google.common.collect.Maps.newHashMap();
        model.put("income", 20);
		model.put("shareholderQuantity", 2);
		model.put("proxyQuantity", 2);
		String render = "<#if ( income gte 50.0 ) || ( shareholderQuantity gte 10 ) || ( proxyQuantity gte 20 )>true</#if>";
		String result = FreeMarkers.renderString(render, model);
		System.out.println(BooleanUtils.toBoolean(result));*/
        Map<String, Integer> model = com.google.common.collect.Maps.newHashMap();
        model.put("ruleNum", 20);
        String render = "yyzz${ruleNum}";
        String result = FreeMarkers.renderString(render, model);
        System.out.println(result);
        FreeMarkers.builder();
        System.out.println(FreeMarkers.render("upload", Maps.newConcurrentMap()));


//		// renderString
//		Map<String, String> model = com.google.common.collect.Maps.newHashMap();
//		model.put("userName", "calvin");
//		String result = FreeMarkers.renderString("hello ${userName}", model);
//		System.out.println(result);
//		// renderTemplate
//		Configuration cfg = FreeMarkers.buildConfiguration("classpath:/");
//		Template template = cfg.getTemplate("testTemplate.ftl");
//		String result2 = FreeMarkers.renderTemplate(template, model);
//		System.out.println(result2);

//		Map<String, String> model = com.google.common.collect.Maps.newHashMap();
//		model.put("userName", "calvin");
//		String result = FreeMarkers.renderString("hello ${userName} ${r'${userName}'}", model);
//		System.out.println(result);
    }

}
