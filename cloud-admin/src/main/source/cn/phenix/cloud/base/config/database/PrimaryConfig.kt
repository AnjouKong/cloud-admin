package cn.phenix.cloud.base.config.database

import cn.phenix.cloud.jpa.GenericJpaRepositoryFactoryBean
import cn.phenix.cloud.jpa.GenericJpaRepositoryImpl
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = GenericJpaRepositoryImpl::class,
        repositoryFactoryBeanClass = GenericJpaRepositoryFactoryBean::class,
        basePackages = arrayOf("cn.phenix.cloud.admin"))
class PrimaryConfig