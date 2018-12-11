package cn.phenix.cloud.base.config.database;

import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2017-11-27 下午3:02
 **/
public class DataSourceBuilder {

    private static final String[] DATA_SOURCE_TYPE_NAMES = new String[] {
            "com.alibaba.druid.pool.DruidDataSource",
            "org.apache.tomcat.jdbc.pool.DataSource",
            "org.apache.commons.dbcp2.BasicDataSource" };

    private Class<? extends DataSource> type;

    private ClassLoader classLoader;

    private Map<String, String> properties = new HashMap<>();

    public static DataSourceBuilder create() {
        return new DataSourceBuilder(null);
    }

    public static DataSourceBuilder create(ClassLoader classLoader) {
        return new DataSourceBuilder(classLoader);
    }

    public DataSourceBuilder(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public DataSource build() {
        Class<? extends DataSource> type = getType();
        DataSource result = BeanUtils.instantiateClass(type);
        maybeGetDriverClassName();
        bind(result);
        return result;
    }

    private void maybeGetDriverClassName() {
        if (!this.properties.containsKey("driverClassName")
                && this.properties.containsKey("url")) {
            String url = this.properties.get("url");
            String driverClass = DatabaseDriver.fromJdbcUrl(url).getDriverClassName();
            this.properties.put("driverClassName", driverClass);
        }
    }

    private void bind(DataSource result) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(
                this.properties);
        ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
        aliases.addAliases("url", "jdbc-url");
        aliases.addAliases("username", "user");
        Binder binder = new Binder(source.withAliases(aliases));
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
    }

    public DataSourceBuilder type(Class<? extends DataSource> type) {
        this.type = type;
        return this;
    }

    public DataSourceBuilder url(String url) {
        this.properties.put("url", url);
        return this;
    }

    public DataSourceBuilder driverClassName(String driverClassName) {
        this.properties.put("driverClassName", driverClassName);
        return this;
    }

    public DataSourceBuilder username(String username) {
        this.properties.put("username", username);
        return this;
    }

    public DataSourceBuilder password(String password) {
        this.properties.put("password", password);
        return this;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends DataSource> findType() {
        if (this.type != null) {
            return this.type;
        }
        for (String name : DATA_SOURCE_TYPE_NAMES) {
            try {
                return (Class<? extends DataSource>) ClassUtils.forName(name,
                        this.classLoader);
            }
            catch (Exception ex) {
                // Swallow and continue
            }
        }
        return null;
    }

    private Class<? extends DataSource> getType() {
        Class<? extends DataSource> type = findType();
        if (type != null) {
            return type;
        }
        throw new IllegalStateException("No supported DataSource type found");
    }

}
