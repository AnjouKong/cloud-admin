package cn.phenix.cloud.base.config

import cn.phenix.cloud.base.config.database.DataSourceBuilder
import cn.phenix.cloud.core.jdbc.MyJdbcTemplate
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource


@Configuration
//@Import(value= arrayOf(DynamicDataSourceRegister::class)) // 注册动态多数据源
class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun primaryDataSource(): DataSource = DataSourceBuilder.create().build()


    @Bean(name = arrayOf("logDataSource"))
    @Qualifier("logDataSource")
    @ConfigurationProperties(prefix = "spring.logsource")
    fun logDataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean(name = arrayOf("logJdbcTemplate"))
    fun secondJdbcTemplate(@Qualifier("logDataSource") dataSource: DataSource): MyJdbcTemplate
            = MyJdbcTemplate(dataSource)
}