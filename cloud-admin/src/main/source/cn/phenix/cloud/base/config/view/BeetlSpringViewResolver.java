package cn.phenix.cloud.base.config.view;

import cn.phenix.cloud.base.config.view.tag.UEditorTag;
import cn.phenix.cloud.base.config.view.tag.UploadTag;
import cn.phenix.cloud.base.utils.DateUtil;
import cn.phenix.cloud.base.utils.ShiroUtil;
import cn.phenix.cloud.base.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beetl.core.GroupTemplate;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringView;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2017-09-25 下午7:39
 **/
public class BeetlSpringViewResolver extends AbstractTemplateViewResolver implements InitializingBean, BeanNameAware {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private String beanName = null;
    private BeetlGroupUtilConfiguration config;
    private GroupTemplate groupTemplate = null;

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setGroupTemplate(GroupTemplate groupTemplate) {
        this.groupTemplate = groupTemplate;
    }

    public BeetlSpringViewResolver() {
        this.setViewClass(BeetlSpringView.class);
    }

    public void afterPropertiesSet() throws NoSuchBeanDefinitionException, SecurityException, NoSuchFieldException {
        if (this.config == null) {
            this.config = this.getApplicationContext().getBean(BeetlGroupUtilConfiguration.class);
            this.groupTemplate = this.config.getGroupTemplate();
            groupTemplate.registerTag("upload", UploadTag.class);
            groupTemplate.registerTag("ueditor", UEditorTag.class);
        }

        if (this.getContentType() == null) {
            String charset = this.groupTemplate.getConf().getCharset();
            this.setContentType("text/html;charset=" + charset);
        }

    }

    protected Class<BeetlSpringView> requiredViewClass() {
        return BeetlSpringView.class;
    }

    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        BeetlSpringView beetlView = (BeetlSpringView) super.buildView(viewName);
        beetlView.setGroupTemplate(this.groupTemplate);
        String suffix = this.getSuffix();
        if (suffix.length() != 0 && viewName.contains("#")) {
            String[] split = viewName.split("#");
            if (split.length > 2) {
                throw new Exception("视图名称有误：" + viewName);
            }

            beetlView.setUrl(this.getPrefix() + split[0] + this.getSuffix() + "#" + split[1]);
        }

        return beetlView;
    }

    public static String redirect(String url) {
        return "redirect:" + url;
    }

    public static String forward(String url) {
        return "forward:" + url;
    }

    public BeetlGroupUtilConfiguration getConfig() {
        return this.config;
    }

    public void setConfig(BeetlGroupUtilConfiguration config) {
        this.config = config;
        this.groupTemplate = config.getGroupTemplate();
        Map<String, Object> shared = new HashMap<>();
        shared.put("shiro", new ShiroUtil());
        shared.put("date", new DateUtil());
        shared.put("base", "");
        shared.put("string", new StringUtil());
        groupTemplate.setSharedVars(shared);
    }

    public void setPrefix(String prefix) {
        this.logger.warn("Beetl不建议使用使用spring前缀，会导致include,layout找不到对应的模板，请使用beetl的配置RESOURCE.ROOT来配置模板根目录");
        super.setPrefix(prefix);
    }
}
